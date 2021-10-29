package com.android.workoutappamelia

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import java.util.*

private const val TAG = "WorkoutFragment"
private const val ARG_WORKOUT_ID = "workout_id"
private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0



class ItemFragment : Fragment(), DatePickerFragment.Callbacks {

    private lateinit var workout: Workout
    private lateinit var titleField: EditText
    private lateinit var locationField: EditText
    private lateinit var dateButton: Button
    private lateinit var groupCheckBox: CheckBox
    private lateinit var individualCheckBox: CheckBox
    private lateinit var startTime: Button
    private lateinit var finishTime: Button
    private val workoutDetailViewModel: WorkoutDetailViewModel by lazy {
        ViewModelProviders.of(this).get(WorkoutDetailViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workout = Workout()
        val workoutId: UUID = arguments?.getSerializable(ARG_WORKOUT_ID) as UUID
        workoutDetailViewModel.loadWorkout(workoutId)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item, container, false)

        titleField = view.findViewById(R.id.activity_title) as EditText
        locationField = view.findViewById(R.id.activity_location) as EditText
        dateButton = view.findViewById(R.id.activity_date) as Button
        groupCheckBox = view.findViewById(R.id.group_activity) as CheckBox
        individualCheckBox = view.findViewById(R.id.individual_activity) as CheckBox
        startTime = view.findViewById(R.id.start_time) as Button
        finishTime = view.findViewById(R.id.finish_time) as Button

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        workoutDetailViewModel.workoutLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { workout ->
                workout?.let {
                    this.workout = workout
                    updateUI()
                }
            })
    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {

            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                //blank
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                workout.title = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
               //blank
            }
        }
        titleField.addTextChangedListener(titleWatcher)

        val placeWatcher = object : TextWatcher {

            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                //blank
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                workout.place = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
                //blank
            }
        }
        locationField.addTextChangedListener(placeWatcher)

        individualCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked ->
                workout.isIndividual = isChecked
            }
        }

        groupCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked ->
                workout.isGroup = isChecked
            }
        }

        dateButton.setOnClickListener {
            DatePickerFragment.newInstance(workout.date).apply {
                setTargetFragment(this@ItemFragment, REQUEST_DATE)
                show(this@ItemFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }

        startTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timeListener = TimePickerDialog.OnTimeSetListener { timePicker: TimePicker, hourOfDay, minute ->
               calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
               calendar.set(Calendar.MINUTE, minute)
               startTime.text = java.text.SimpleDateFormat("HH:mm"). format(calendar.time)
            }
            TimePickerDialog(context, timeListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),false).show()
        }

        finishTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timeListener = TimePickerDialog.OnTimeSetListener { timePicker: TimePicker, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                finishTime.text = java.text.SimpleDateFormat("HH:mm"). format(calendar.time)
            }
            TimePickerDialog(context, timeListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),false).show()
        }

    }

    override fun onStop() {
        super.onStop()
        workoutDetailViewModel.saveWorkout(workout)
    }

    override fun onDateSelected(date: Date) {
        workout.date = date
        updateUI()
    }




    private fun updateUI() {
        titleField.setText(workout.title)
        locationField.setText(workout.place)
        dateButton.text = DateFormat.format("EEEE, dd MMM yyyy", this.workout.date)
        groupCheckBox.apply {
            isChecked = workout.isGroup
            jumpDrawablesToCurrentState()
        }
        individualCheckBox.apply{
            isChecked = workout.isIndividual
            jumpDrawablesToCurrentState()
        }
    }

    companion object {

        fun newInstance(workoutId: UUID): ItemFragment {
            val args  = Bundle().apply {
                putSerializable(ARG_WORKOUT_ID, workoutId)
            }
            return ItemFragment().apply {
                arguments = args
            }
        }
    }




}



