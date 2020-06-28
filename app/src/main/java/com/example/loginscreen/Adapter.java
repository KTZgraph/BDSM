package com.example.loginscreen;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    LayoutInflater inflater;
    List<Note> allNotes;

    Adapter(Context context, List<Note> allNotes){
        this.inflater = LayoutInflater.from(context);
        this.allNotes = allNotes;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String date = allNotes.get(position).getDate();
        String time = allNotes.get(position).getTime();

        holder.noteDate.setText(date);
        holder.noteTime.setText(time);

    }

    @Override
    public int getItemCount() {
        return allNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        EditText noteRawPassword;
        TextView noteDate;
        TextView noteTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteRawPassword = itemView.findViewById(R.id.oldRawNotePassword);
            // TODO: sprawdzanie czy user podał dobre haslo; w sumie to ze zlym haselm nie rozszyfruje wiec na razie sie nie przjmuje
            noteDate = itemView.findViewById(R.id.noteDate);
            noteTime = itemView.findViewById(R.id.noteTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentDetailsActivity = new Intent(v.getContext(), DetailsActivity.class);
                    intentDetailsActivity.putExtra("noteID",allNotes.get(getAdapterPosition()).getID());
                    intentDetailsActivity.putExtra("noteRawPassword", noteRawPassword.getText().toString()); // TODO przekazanie hasła z notatki
                    v.getContext().startActivity(intentDetailsActivity);
                }
            });

        }
    }
}
