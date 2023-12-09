package com.example.compquiz
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var question:TextView
    private lateinit var progressBar: ProgressBar
    private var i = 0
    private var score = 0
    private lateinit var scoreView:TextView
    private lateinit var option1:TextView
    private lateinit var option2:TextView
    private lateinit var option3:TextView
    private lateinit var option4:TextView
    private lateinit var result:ArrayList<Result>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progressBar)
        scoreView = findViewById(R.id.score)
        question = findViewById(R.id.question)
        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)
        option4 = findViewById(R.id.option4)
        result = ArrayList()

        //result.add(Result("abc","Option 4","easy", listOf("Option 1","Option 2","Option 3","Option 4"),"Vandan Nandwana Is A brilliant Programmer Uski Jitni Tarif Ki Jaye Utni Kam Hain ","Str"))
        val BASE_URL = "https://opentdb.com/"
        val res = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        res.create(ApiInterface::class.java)
            .getQuestion().enqueue(object : Callback<QuestionModel?> {
                override fun onResponse(
                    call: Call<QuestionModel?>,
                    response: Response<QuestionModel?>
                ) {
                    val body = response.body()
                    val results = body?.results

                    if(results != null){

                        for(r in results){
                            result.add(Result(r.category,r.correct_answer,r.difficulty,r.incorrect_answers,r.question,r.type))
                            progressBar.visibility = View.GONE
                        }

                        setQuestion()


                        option1.setOnClickListener {
                            nextQuestion(option1.text.toString())
                            Log.d("Vandan",result[i].correct_answer+" And "+option1.text.toString())
                        }

                        option2.setOnClickListener {
                            nextQuestion(option2.text.toString())
                        }

                        option3.setOnClickListener {
                            nextQuestion(option3.text.toString())
                        }

                        option4.setOnClickListener {
                            nextQuestion(option4.text.toString())
                        }


                    }

                }

                override fun onFailure(call: Call<QuestionModel?>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })


    }

    private fun setQuestion() {
        if(i == 0) {
            question.text = result[i].question
            option1.text = result[i].incorrect_answers[0]
            option2.text = result[i].incorrect_answers[1]
            option3.text = result[i].incorrect_answers[2]
            option4.text = result[i].correct_answer
        }else {
            when (Random.nextInt(3)) {
                0 -> {
                    question.text = result[i].question
                    option1.text = result[i].incorrect_answers[0]
                    option2.text = result[i].correct_answer
                    option3.text = result[i].incorrect_answers[2]
                    option4.text = result[i].incorrect_answers[1]
                }

                1 -> {
                    question.text = result[i].question
                    option1.text = result[i].incorrect_answers[0]
                    option2.text = result[i].incorrect_answers[1]
                    option3.text = result[i].correct_answer
                    option4.text = result[i].incorrect_answers[2]
                }

                2 -> {
                    question.text = result[i].question
                    option1.text = result[i].incorrect_answers[0]
                    option2.text = result[i].correct_answer
                    option3.text = result[i].incorrect_answers[2]
                    option4.text = result[i].incorrect_answers[1]
                }

                3 -> {
                    question.text = result[i].question
                    option1.text = result[i].correct_answer
                    option2.text = result[i].incorrect_answers[1]
                    option3.text = result[i].incorrect_answers[2]
                    option4.text = result[i].incorrect_answers[0]
                }
            }
        }
    }

    private fun nextQuestion(option_text: String) {

            if(i >=9){
                if(option_text == result[i].correct_answer){
                    score++
                    scoreView.text = "Score : $score"
                }
                Toast.makeText(this@MainActivity,"Last Question",Toast.LENGTH_SHORT).show()
            }
            else{
                if(option_text == result[i].correct_answer){
                    i++
                    setQuestion()
                    score++
                    scoreView.text = "Score : $score"
                }
                else{
                    i++
                    setQuestion()
                }
            }

    }

}