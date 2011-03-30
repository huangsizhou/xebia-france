/*
 * Copyright 2008-2010 Xebia and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.xebia.monitoring.demo.payment;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.payment.common.money.MonetaryAmount;
import org.springframework.payment.common.order.Order;
import org.springframework.payment.core.InvalidCardException;
import org.springframework.payment.core.LostOrStolenCardException;
import org.springframework.payment.core.MissingOrInvalidDataException;
import org.springframework.payment.core.PaymentProcessingException;
import org.springframework.payment.core.PaymentTransaction;
import org.springframework.payment.core.TransactionAmountException;
import org.springframework.payment.creditcard.CreditCardService;

@ManagedResource
public class CreditCardServiceMonitoringImpl implements CreditCardService {

    private final AtomicInteger activeInvocationCounter = new AtomicInteger();

    private CreditCardService creditCardService;

    private final AtomicInteger invalidCardExceptionCounter = new AtomicInteger();

    private final AtomicInteger lostOrStolenCardExceptionCounter = new AtomicInteger();

    private final AtomicInteger missingOrInvalidDataExceptionCounter = new AtomicInteger();

    private final AtomicInteger paymentProcessingExceptionCounter = new AtomicInteger();

    private final AtomicInteger purchaseInvocationCounter = new AtomicInteger();

    private final AtomicLong purchaseInvocationDurationInNanosCounter = new AtomicLong();

    private final AtomicInteger slowRequestCounter = new AtomicInteger();

    private AtomicLong slowRequestThresholdInNanos = new AtomicLong(TimeUnit.NANOSECONDS.convert(1, TimeUnit.SECONDS));

    private final AtomicInteger threeDSecureVerificationExceptionCounter = new AtomicInteger();

    private final AtomicInteger timeoutExceptionCounter = new AtomicInteger();

    private final AtomicInteger transactionAmountExceptionCounter = new AtomicInteger();

    public CreditCardServiceMonitoringImpl() {
        super();
    }

    public CreditCardServiceMonitoringImpl(CreditCardService creditCardService) {
        super();
        this.creditCardService = creditCardService;
    }

    @Override
    public PaymentTransaction authorize(MonetaryAmount total, Order order, String requestId) {
        return creditCardService.authorize(total, order, requestId);
    }

    @Override
    public PaymentTransaction capture(MonetaryAmount total, PaymentTransaction authTransaction) {
        return creditCardService.capture(total, authTransaction);
    }

    @ManagedAttribute
    public int getActiveInvocationCount() {
        return activeInvocationCounter.get();
    }

    public CreditCardService getCreditCardService() {
        return creditCardService;
    }

    @ManagedAttribute
    public int getInvalidCardExceptionCount() {
        return invalidCardExceptionCounter.get();
    }

    @ManagedAttribute
    public int getLostOrStolenCardExceptionCount() {
        return lostOrStolenCardExceptionCounter.get();
    }

    @ManagedAttribute
    public int getMissingOrInvalidDataExceptionCount() {
        return missingOrInvalidDataExceptionCounter.get();
    }

    @ManagedAttribute
    public int getPaymentProcessingExceptionCount() {
        return paymentProcessingExceptionCounter.get();
    }

    @ManagedAttribute
    public int getPurchaseInvocationCounter() {
        return purchaseInvocationCounter.get();
    }

    @ManagedAttribute
    public long getPurchaseInvocationDurationInNanosCounter() {
        return purchaseInvocationDurationInNanosCounter.get();
    }

    @ManagedAttribute
    public int getSlowRequestCount() {
        return slowRequestCounter.get();
    }

    /**
     * Human readable version of {@link #getSlowRequestThresholdInNanos()}
     */
    @ManagedAttribute
    public long getSlowRequestThresholdInMillis() {
        return TimeUnit.MILLISECONDS.convert(slowRequestThresholdInNanos.get(), TimeUnit.NANOSECONDS);
    }

    @ManagedAttribute
    public long getSlowRequestThresholdInNanos() {
        return slowRequestThresholdInNanos.get();
    }

    @ManagedAttribute
    public int getThreeDSecureVerificationExceptionCount() {
        return threeDSecureVerificationExceptionCounter.get();
    }

    @ManagedAttribute
    public int getTimeoutExceptionCount() {
        return timeoutExceptionCounter.get();
    }

    @ManagedAttribute
    public int getTransactionAmountExceptionCount() {
        return transactionAmountExceptionCounter.get();
    }

    @Override
    public PaymentTransaction purchase(MonetaryAmount total, Order order, String requestId) {

        long nanosBefore = System.nanoTime();

        activeInvocationCounter.incrementAndGet();
        PaymentTransaction paymentTransaction;
        try {
            paymentTransaction = creditCardService.purchase(total, order, requestId);
        } catch (ThreeDSecureVerificationException e) {
            threeDSecureVerificationExceptionCounter.incrementAndGet();
            throw e;
        } catch (LostOrStolenCardException e) {
            lostOrStolenCardExceptionCounter.incrementAndGet();
            throw e;
        } catch (InvalidCardException e) {
            invalidCardExceptionCounter.incrementAndGet();
            throw e;
        } catch (MissingOrInvalidDataException e) {
            missingOrInvalidDataExceptionCounter.incrementAndGet();
            throw e;
        } catch (PaymentProcessingException e) {
            paymentProcessingExceptionCounter.incrementAndGet();
            throw e;
        } catch (TimeoutPaymentException e) {
            timeoutExceptionCounter.incrementAndGet();
            throw e;
        } catch (TransactionAmountException e) {
            transactionAmountExceptionCounter.incrementAndGet();
            throw e;
        } finally {
            activeInvocationCounter.decrementAndGet();
            purchaseInvocationCounter.incrementAndGet();
            long durationInNanos = System.nanoTime() - nanosBefore;
            purchaseInvocationDurationInNanosCounter.addAndGet(durationInNanos);

            if (durationInNanos > slowRequestThresholdInNanos.get()) {
                slowRequestCounter.incrementAndGet();
            }
        }
        return paymentTransaction;
    }

    @Required
    public void setCreditCardService(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @ManagedAttribute
    public void setSlowRequestThresholdInMillis(long slowRequestThresholdInMillis) {
        this.slowRequestThresholdInNanos.set(TimeUnit.NANOSECONDS.convert(slowRequestThresholdInMillis, TimeUnit.MILLISECONDS));
    }

    @ManagedAttribute
    public void setSlowRequestThresholdInNanos(long slowRequestThresholdInNanos) {
        this.slowRequestThresholdInNanos.set(slowRequestThresholdInNanos);
    }
}
