package hu.unideb.inf.roomshoppinglist;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import java.util.List;

import hu.unideb.inf.roomshoppinglist.databinding.ActivityMainBinding;
import hu.unideb.inf.roomshoppinglist.model.ShoppingListDatabase;
import hu.unideb.inf.roomshoppinglist.model.ShoppingListItem;

public class MainActivity extends AppCompatActivity {

    ShoppingListDatabase shoppingListDatabase;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        shoppingListDatabase = Room.databaseBuilder(this,
                        ShoppingListDatabase.class,
                        "shoppinglist_db")
                .fallbackToDestructiveMigration(true)
                .build();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        shoppingListDatabase.shoppinglistDAO().getAllItems().observe(
                this,
                shoppingListItems ->
                                    binding.recyclerView.setAdapter(new ViewAdapter(shoppingListItems)
                )
        );
    }

    public void addItem(View view) {
        new Thread(
                () -> {
                    ShoppingListItem sli = new ShoppingListItem();
                    sli.setName(binding.newItemEditText.getText().toString());
                    shoppingListDatabase.shoppinglistDAO().insertListItem(sli);

                    /*String listText = shoppingListDatabase.shoppinglistDAO().getAllItems().toString();
                    Log.d("CheckDB", listText);
                    runOnUiThread(() -> binding.shoppingListTextView.setText(listText));*/
                }
        ).start();
    }
}