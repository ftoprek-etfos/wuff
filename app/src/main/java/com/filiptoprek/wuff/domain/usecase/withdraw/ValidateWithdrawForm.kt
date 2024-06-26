package com.filiptoprek.wuff.domain.usecase.withdraw

import com.filiptoprek.wuff.domain.model.reload.Reload
import com.filiptoprek.wuff.domain.model.withdraw.Withdraw
import com.filiptoprek.wuff.domain.model.withdraw.WithdrawProfile
import com.filiptoprek.wuff.domain.repository.auth.AuthRepository
import com.filiptoprek.wuff.domain.repository.withdraw.WithdrawRepository
import com.filiptoprek.wuff.domain.usecase.reload.ValidateCVVUseCase
import com.filiptoprek.wuff.domain.usecase.reload.ValidateCardDateUseCase

class ValidateWithdrawForm(
    private val validateWithdraw: ValidateWIthdraw,
    private val validateWithdrawProfile: ValidateWithdrawProfile,
) {
    suspend fun validateForm(withdraw: Withdraw, withdrawProfile: WithdrawProfile, withdrawRepository: WithdrawRepository, authRepository: AuthRepository): Int {
        val isWithdrawValid = validateWithdraw(withdraw, withdrawRepository, authRepository)
        val isWithdrawProfileValid = validateWithdrawProfile(withdrawProfile)

        if(!isWithdrawValid)
            return -1

        if(!isWithdrawProfileValid)
            return -2

        return 1
    }
}