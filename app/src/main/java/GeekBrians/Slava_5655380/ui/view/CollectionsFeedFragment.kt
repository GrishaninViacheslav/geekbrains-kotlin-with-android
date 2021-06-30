package GeekBrians.Slava_5655380.ui.view

import GeekBrians.Slava_5655380.R
import GeekBrians.Slava_5655380.databinding.CollectionsFeedFragmentBinding
import android.Manifest
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


class CollectionsFeedFragment : Fragment() {
    private val REQUEST_CODE = 42
    private var _binding: CollectionsFeedFragmentBinding? = null
    private val binding get() = _binding!!
    private val handler = Handler(Looper.getMainLooper())

    private fun checkPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS)
                        == PackageManager.PERMISSION_GRANTED
                        &&
                        ContextCompat.checkSelfPermission(it, Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_GRANTED
                -> {
                    getContacts()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)
                        ||
                        shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)
                -> {
                    AlertDialog.Builder(it)
                        .setTitle("Доступ к контактам")
                        .setMessage("Для работы данного меню приложению необходимы разрешения на чтения контактов и совершение вызовов")
                        .setPositiveButton("Дать разрешения") { _, _ ->
                            requestPermission()
                        }
                        .setNegativeButton("Отказать в разрешениях") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                }
                else -> {
                    requestPermission()
                }
            }
        }
    }

    private fun requestPermission() {
        requestPermissions(
            arrayOf(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE
            ), REQUEST_CODE
        )
    }

    private fun getContacts() {
        context?.let {
            Thread {
                val contentResolver: ContentResolver = it.contentResolver
                val cursorWithContacts: Cursor = contentResolver.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    null,
                    null,
                    null,
                    ContactsContract.Contacts.DISPLAY_NAME + " ASC"
                ) ?: return@Thread
                cursorWithContacts.moveToFirst()
                do {
                    val name: String = cursorWithContacts.getString(
                        cursorWithContacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    )
                    val id: String = cursorWithContacts.getString(
                        cursorWithContacts.getColumnIndex(ContactsContract.Contacts.NAME_RAW_CONTACT_ID)
                    )
                    val phones: Cursor = contentResolver
                        .query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID + " = " + id,
                            null,
                            null
                        )!!
                    if (phones != null) {
                        while (phones.moveToNext()) {
                            val phoneNumber = phones.getString(
                                phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            )
                            Log.d("[MYLOG]", "addView: $name $phoneNumber")
                            addView(it, name, phoneNumber)
                        }
                        phones.close()
                    }
                } while (cursorWithContacts.moveToNext())
                cursorWithContacts.close()
            }.start()
        }
    }

    private fun addView(context: Context, name: String, phoneNumber: String) {
        val textView = AppCompatTextView(context).apply {
            text = "$name\n$phoneNumber"
            textSize = resources.getDimension(R.dimen.text_small_size)
            setOnClickListener {
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:" + phoneNumber) //change the number
                startActivity(callIntent)
            }
        }
        handler.post {
            binding.containerForContacts.addView(textView)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CollectionsFeedFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CollectionsFeedFragmentBinding.inflate(
            inflater, container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                ) {
                    getContacts()
                } else {
                    AlertDialog.Builder(context)
                        .setTitle("Доступ к контактам")
                        .setMessage("Данное меню не функционирует так как отсутствует разрешение на чтение контактов и своершения вызовов")
                        .setNegativeButton("Закрыть") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                }
                return
            }
        }
    }
}
