package com.example.quizapp
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.QuizActivity
import com.example.quizapp.ListQuestionsActivity
import com.example.quizapp.R

class QuestionAdapter(
    private val questions: MutableList<QuizActivity.QuizQuestion>,
    private val onQuestionClick: (QuizActivity.QuizQuestion) -> Unit
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val questionText: TextView = itemView.findViewById(R.id.questionText)

        fun bind(question: QuizActivity.QuizQuestion) {
            questionText.text = question.questionText

            // Set an item click listener to handle question selection
            itemView.setOnClickListener {
                onQuestionClick(question)
                itemView.setOnClickListener {
                    onQuestionClick(question)
                    Log.d("ListQuestionsActivity", "Question clicked: ${question.questionText}")
                }

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.question_item, parent, false)
        return QuestionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(question)
    }

    override fun getItemCount(): Int {
        return questions.size
    }
}
