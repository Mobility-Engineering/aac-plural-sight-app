package com.dexcom.sdk.aac_fullcontentapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.dexcom.sdk.aac_fullcontentapp.databinding.ActivityMainBinding

class NoteActivity : AppCompatActivity() {


/*private var originalNoteText: String? = null
private var orginalNoteTitle: String? = null
private var originalNoteCourseId: String? = "" */
private var shouldFinish: Boolean = false
private var notePosition: Int = 0
private var isNewNote: Boolean = false
private lateinit var noteInfo: NoteInfo
private lateinit var textNoteTitle: EditText
private lateinit var textNoteText: EditText
private lateinit var spinner: Spinner
private lateinit var binding: ActivityMainBinding
private lateinit var viewModel:NoteActivityViewModel
override fun onCreate(savedInstanceState: Bundle?) {
   super.onCreate(savedInstanceState)
   binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


    viewModel = ViewModelProvider(this).get(NoteActivityViewModel::class.java)

   spinner = binding.spinnerCourses
   val courses: List<CourseInfo> = DataManager.instance!!.courses
   val adapterCourses: ArrayAdapter<CourseInfo> =
       ArrayAdapter(this, android.R.layout.simple_spinner_item, courses)
   spinner.adapter = adapterCourses

    if (viewModel.isNewlyCreated && savedInstanceState !== null) { //being equal to null implies that the activity was created for the first time, when that is not the case simply
        //restore viewModel state from Bundle
        //This is an overkill as when it is destroyed onConfigurationChanges viewModel will already hold the used value
        viewModel.restoreState(savedInstanceState)
    }
        viewModel.isNewlyCreated = false
   readDisplayStates() //get noteInfo in order to populate the Views


   saveOriginalNoteValues()  //Store backup of original noteInfo values used onCancel()

   textNoteTitle = binding.textNoteTitle
   textNoteText = binding.textNodeText
   displayNote(spinner, textNoteTitle, textNoteText)
}

private fun saveOriginalNoteValues() {
   if (isNewNote)
       return
   viewModel.originalNoteCourseId = noteInfo.course?.courseId
   viewModel.originalNoteTitle = noteInfo.title
   viewModel.originalNoteText = noteInfo.text
}

private fun displayNote(spinner: Spinner, textNoteTitle: EditText, textNoteText: EditText) {
   val courses: List<CourseInfo> = DataManager.instance!!.courses
   val courseIndex = courses.indexOf(noteInfo?.course)
   spinner.setSelection(courseIndex)
   textNoteTitle.setText(noteInfo?.title)
   textNoteText.setText(noteInfo?.text)
}

override fun onPause() {
   super.onPause()
   if (shouldFinish) {
       if (isNewNote) {
           DataManager.instance?.removeNote(notePosition)
       } else {
       storePreviousNoteValues()
       }
   } else
       saveNote()
}

private fun storePreviousNoteValues() {
   //Save the actual data that lays behind courtains nbut keep showing new values on display
   //as it will update them back from Spinner and EditText when saveNote()
     val course =  viewModel.originalNoteCourseId?.let { DataManager.instance?.getCourse(it) }
   noteInfo.course = course
   noteInfo.text = viewModel.originalNoteText
   noteInfo.title = viewModel.originalNoteTitle
}

private fun saveNote() {
   noteInfo?.course = spinner.selectedItem as CourseInfo
   noteInfo?.title = textNoteTitle?.getText().toString()
   noteInfo?.text = textNoteText?.getText().toString()
}

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.isNewlyCreated = false
    }


    private fun readDisplayStates() {
   val intent = getIntent()
   val position = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET)
   isNewNote = position == POSITION_NOT_SET
   //App Step - Create a new node
   if (isNewNote) {
       createNewNote()
   } else
       noteInfo = DataManager.instance!!.notes.get(position)
   //App step->Parcel extra removal
   //noteInfo = intent.getParcelableExtra<NoteInfo>(Companion.NOTE_INFO)
}

private fun createNewNote() {
   val dm = DataManager.instance
   notePosition = dm!!.createNewNote()
   noteInfo = dm?.notes?.get(notePosition)
}

override fun onCreateOptionsMenu(menu: Menu?): Boolean {
   menuInflater.inflate(R.menu.menu_note, menu)
   return true
}

override fun onOptionsItemSelected(item: MenuItem): Boolean {

   val id = item.itemId
   when (id) {
       R.id.action_send_mail -> {
           sendMail()
           return true
       }
       R.id.action_cancel -> {
           shouldFinish = true
           finish()
       }
   }
   return false

}

private fun sendMail() {
   var course = spinner.selectedItem as CourseInfo
   var subject = textNoteTitle.text.toString()
   var text =
       "Checkout what I learned in the Pluralsight course \"" + course.title + "\"\n" + textNoteText.text.toString()
   val intent = Intent(Intent.ACTION_SEND)
   intent.setType("message/rfc2822")
   intent.putExtra(Intent.EXTRA_SUBJECT, subject)
   intent.putExtra(Intent.EXTRA_TEXT, text)
   startActivity(intent)
}

companion object {
   const val NOTE_INFO = "NOTE_INFO"
   const val NOTE_POSITION = "NOTE_POSITION"
   const val POSITION_NOT_SET = -1
}
}