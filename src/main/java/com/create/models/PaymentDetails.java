package com.create.models;

import com.create.domain.PaymentStatus;

import jakarta.persistence.Entity;


public class PaymentDetails {
	
	private PaymentStatus paymentStatus;
	private String paymentId;
	private String razorpayPaymentLinkId;
	private String razorpayPaymentLinkReferenceId;
	private String razorpayPaymentLinkStatus;
	private String razorpayPaymentId;
	

}
