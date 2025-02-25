package com.mustafacanyucel.fireflyiiishortcuts.services.dialog

import com.mustafacanyucel.fireflyiiishortcuts.model.EventData

interface IDialogService {
    fun showDialogSnackbar(eventData: EventData)
}