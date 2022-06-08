package com.dexcom.sdk.aac_fullcontentapp

import android.os.Bundle
import androidx.lifecycle.ViewModel

class NoteActivityViewModel : ViewModel() {
    var originalNoteText: String? = null
    var originalNoteTitle: String? = null
    var originalNoteCourseId: String? = ""
    var isNewlyCreated: Boolean = true


    companion object Constants {
        const val ORIGINAL_NOTE_COURSE_ID =
            "com.dexcom.sdk.aac_fullcontentapp.ORIGINAL_NOTE_COURSE_ID"
        const val ORIGINAL_NOTE_COURSE_TITLE =
            "com.dexcom.sdk.aac_fullcontentapp.ORIGINAL_NOTE_COURSE_TITLE"
        const val ORIGINAL_NOTE_COURSE_TEXT =
            "com.dexcom.sdk.aac_fullcontentapp.ORIGINAL_NOTE_COURSE_TEXT"
    }

    fun saveState(outState: Bundle) {
        outState.putString(ORIGINAL_NOTE_COURSE_ID, originalNoteCourseId)
        outState.putString(ORIGINAL_NOTE_COURSE_TITLE, originalNoteTitle)
        outState.putString(ORIGINAL_NOTE_COURSE_TEXT, originalNoteText)
    }


    fun restoreState(inState: Bundle) {
        originalNoteCourseId = inState.getString(ORIGINAL_NOTE_COURSE_ID)
        originalNoteTitle = inState.getString(ORIGINAL_NOTE_COURSE_TITLE)
        originalNoteText = inState.getString(ORIGINAL_NOTE_COURSE_TEXT)
    }
}
