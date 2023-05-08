package com.moengage.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.moengage.example.databinding.FragmentHomeBinding
import com.moengage.sample.payment.sdk.PaymentSDK
import com.moengage.sample.payment.sdk.model.PaymentData
import com.moengage.sample.payment.sdk.model.UserData

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.checkoutBtn.setOnClickListener {
            checkout()
        }
    }

    private fun checkout() {
        val name = binding.nameEt.text.toString()
        val age = binding.ageEt.text.toString()
        val phoneNumber = binding.phoneNumberEt.text.toString()
        val emailId = binding.emailEt.text.toString()
        val currency = binding.currencySpinner.selectedItem.toString()
        val amount = binding.amountEt.text.toString()
        if (name.isBlank()) {
            binding.nameEt.error = "Enter Valid Name"
            return
        }
        if (age.isBlank() || age.toInt() > 100 || age.toInt() < 0) {
            binding.ageEt.error = "Enter Valid Age"
            return
        }
        if (emailId.isBlank()) {
            binding.emailEt.error = "Enter Valid Email"
            return
        }
        if (phoneNumber.length < 10) {
            binding.phoneNumberEt.error = "Enter Valid Phone Number"
            return
        }
        if (amount.isBlank()) {
            binding.amountEt.error = "Enter Valid Amount"
            return
        }
        val userData = UserData(name, phoneNumber, emailId, age.toInt(), 11.0002, 22.9999)
        val paymentData = PaymentData(amount.toDouble(), currency)


        /**
         * For Demo Purposes, we are navigating to Payment Checkout by passing user and payment details
         * */
        PaymentSDK.checkoutPayment(requireContext(), userData, paymentData)
    }

}