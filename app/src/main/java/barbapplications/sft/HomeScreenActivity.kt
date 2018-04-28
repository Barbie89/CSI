package barbapplications.sft

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home_screen.*



class HomeScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        btnOpenCaseMenu.setOnClickListener {
            val popup= PopupMenu(this,btnOpenCaseMenu)
            popup.inflate(R.menu.case_menu)
            popup.setOnMenuItemClickListener {
                Toast.makeText(this, it.title, Toast.LENGTH_SHORT).show()
                true
            }
            popup.show()
        }

        val CreateCase:Button = findViewById(R.id.btnCreateCase)
        CreateCase.setOnClickListener{
            val intent = Intent (this,NewCaseActivity:: class.java)
            startActivity(intent)

        }


    }

}

