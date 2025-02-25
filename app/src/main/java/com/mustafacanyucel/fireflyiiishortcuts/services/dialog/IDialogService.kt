package com.mustafacanyucel.fireflyiiishortcuts.services.dialog

interface IDialogService {
    fun showErrorSnackbar(message:String, action: (() -> Unit)? = null, actionTitle:String? = null)
}