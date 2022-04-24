package com.bapas.agent.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.DatePicker
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bapas.agent.R
import com.bapas.agent.databinding.ItemAddUserCardBinding
import com.bapas.agent.fragment.FragAgentHome
import com.bapas.agent.model.AddCardModel
import com.bapas.agent.utility.Utils
import com.bapas.agent.utility.setSafeOnClickListener
import com.google.gson.Gson

class AdapterCardDetails(
    private var ctx: FragAgentHome,
    private var arr: ArrayList<AddCardModel>,
    private val listener: (Int, String) -> Unit
) :
    RecyclerView.Adapter<AdapterCardDetails.MyViewHolder>() {
    lateinit var bind: ItemAddUserCardBinding
    private var isSucess = false
//    private var pos = 0

    inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var edtName = bind.edtName

        var edtAadhar = bind.edtAadhar
        var edtMobile = bind.edtMobile
        var edtLocation = bind.edtLocation
        var rbGenderMale = bind.rbGenderMale
        var rbGenderFemale = bind.rbGenderFemale

        /* var tvNumber = bind.tvNumber
         var tvGender = bind.tvGender
         var tvBod = bind.tvBod
         var tvLocation = bind.tvLocation*/
        var tvPhoto = bind.tvPhoto
        var tvAdharPhoto = bind.tvAdharPhoto
        var btnDatePicker = bind.btnDatePicker
        var imgAdharPhoto = bind.imgAdharPhoto
        var imgPhoto = bind.imgPhoto
        var tvMember = bind.tvMember
    }

    fun addAll(arrList: ArrayList<AddCardModel>) {
        this.arr = arrList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        bind = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_add_user_card, parent, false
        )
        return MyViewHolder(bind.root)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.apply {
            val frag = FragAgentHome()
            val data = arr[position]

//            val ctx = holder.itemView.context
            tvMember.text = data.member
            imgPhoto.setSafeOnClickListener {
                frag.startCamera("1", position, ctx)
            }

            imgAdharPhoto.setSafeOnClickListener {
                frag.startCamera("2", position, ctx)
            }
            if (data.date_of_born.isNotEmpty()) {
                btnDatePicker.setText(data.date_of_born)
            }

            btnDatePicker.setSafeOnClickListener {
                 openDialog(ctx, position)
            }
            tvAdharPhoto.text = data.adharName
            tvPhoto.text = data.photoName
            edtName.setText(data.name)
            edtName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {

                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    arr[holder.adapterPosition].name = charSequence.toString()
                }

                @SuppressLint("DefaultLocale")
                override fun afterTextChanged(editable: Editable) {

                }
            })
            edtAadhar.setText(data.adhar_number)
            edtAadhar.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {

                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    arr[holder.adapterPosition].adhar_number = charSequence.toString()
                }

                @SuppressLint("DefaultLocale")
                override fun afterTextChanged(editable: Editable) {

                }
            })
            edtMobile.setText(data.contact_number)
            edtMobile.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {

                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    arr[holder.adapterPosition].contact_number = charSequence.toString()
                }

                @SuppressLint("DefaultLocale")
                override fun afterTextChanged(editable: Editable) {

                }
            })
            edtLocation.setText(data.location)
            edtLocation.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {

                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    arr[holder.adapterPosition].location = charSequence.toString()
                }

                @SuppressLint("DefaultLocale")
                override fun afterTextChanged(editable: Editable) {

                }
            })
            if (arr[position].gender == "male") {
                rbGenderMale.isChecked = true
            } else if (arr[position].gender == "female") {
                rbGenderFemale.isChecked = true
            }

            rbGenderMale.setOnCheckedChangeListener { p0, p1 ->
                if (p1) {
                    rbGenderFemale.isChecked = false
                    arr[position].gender = "male"
                }
            }
            rbGenderFemale.setOnCheckedChangeListener { p0, p1 ->
                if (p1) {
                    rbGenderMale.isChecked = false
                    arr[position].gender = "female"
                }
            }
        }
    }
    fun openDialog(ctx: FragAgentHome, position: Int) {
        val mDialog = Dialog(ctx.requireContext(), R.style.AlertDialogCustom)
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setContentView(R.layout.dialog_date_picker)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        mDialog.window?.setLayout(width, height)
        mDialog.window?.attributes?.windowAnimations ?: R.style.dialog_animation
        mDialog.setCanceledOnTouchOutside(false)
        mDialog.setCancelable(true)
        val dp: DatePicker = mDialog.findViewById(R.id.dp)
        val btnOk: AppCompatButton = mDialog.findViewById(R.id.btnOk)
        val btnCancel: AppCompatButton = mDialog.findViewById(R.id.btnCancel)

        btnOk.setSafeOnClickListener {
            val day = dp.dayOfMonth.toString()
            val mon = dp.month.toString()
            val year = dp.year.toString()
            val date = "$day/$mon/$year"

//            arrList[position].date_of_born = date
//            adapter?.notifyDataSetChanged()
//            if (adapter == null) {
//                Utils.showToast(ctx.requireActivity(), "Null")
//            } else Utils.showToast(ctx.requireActivity(), "Not Null")
            setBDate(date, position)
//            mBind.btnDatePicker.setText(date)
            mDialog.cancel()
        }
        btnCancel.setSafeOnClickListener {
            mDialog.cancel()
        }

        mDialog.show()
    }

    override fun getItemCount(): Int {
        return arr.size
    }

    fun getAllData(): ArrayList<AddCardModel> {
        Log.e("btnSubmit: ", Gson().toJson(arr))

        for (i in 0 until arr.size) {
            if (arr[i].name.isEmpty()) {
                isSucess = false
                Utils.showToast(ctx.requireActivity(), "Enter Name" + (i + 1))
            } else if (arr[i].adhar_number.isEmpty()) {
                Utils.showToast(ctx.requireActivity(), "Enter Aadhar Card Number" + (i + 1))
            } else if (arr[i].adhar == null) {
                Utils.showToast(ctx.requireActivity(), "Select Aadhar Card Image" + (i + 1))
            } else if (arr[i].photo == null) {
                Utils.showToast(ctx.requireActivity(), "Select Customer Image" + (i + 1))
            } else if (arr[i].contact_number.isEmpty()) {
                Utils.showToast(ctx.requireActivity(), "Enter Contact Number" + (i + 1))
            } else if (arr[i].location.isEmpty()) {
                Utils.showToast(ctx.requireActivity(), "Enter Location" + (i + 1))
            } else if (arr[i].gender.isEmpty()) {
                Utils.showToast(ctx.requireActivity(), "Select Gender" + (i + 1))
            } else if (arr[i].date_of_born.isEmpty()) {
                Utils.showToast(ctx.requireActivity(), "Select Date Of Birth" + (i + 1))
            } else isSucess = true
        }
        return if (isSucess) arr
        else arrayListOf()
//        else arr
    }

    fun addPhoto(uri: Uri, pos: Int) {
        for (i in 0 until arr.size) {
            if (i == pos) {
                arr[pos].photo = uri.toString()
                arr[pos].photoName = uri.lastPathSegment.toString()
            }
        }
        this.notifyDataSetChanged()
    }

    fun addAdhar(uri: Uri, pos: Int) {
        for (i in 0 until arr.size) {
            if (i == pos) {
                arr[pos].adhar = uri.toString()
                arr[pos].adharName = uri.lastPathSegment.toString()
            }
        }
        this.notifyDataSetChanged()
    }

    fun setBDate(date: String, position: Int) {
        for (i in 0 until arr.size) {
            if (i == position) {
                arr[position].date_of_born = date
            }
        }
        this.notifyDataSetChanged()
    }
}