package com.myselfapps.rav.slovarik;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewWordAdapter extends RecyclerView.Adapter<RecyclerViewWordAdapter.WordViewHolder> {
    private List<Word> words;
    RecyclerViewWordAdapter(List<Word> words) {
        this.words = words;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new WordViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.word_card_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        WordViewHolder.firstName.setText(words.get(position).getId()+" - "+words.get(position).getPrimary());//Убрать по завершении
        WordViewHolder.translation.setText(words.get(position).getSecondary());
        WordViewHolder.transcription.setText("[" + words.get(position).getTranscription() + "]");
        WordViewHolder.gender.setText(words.get(position).getGender());
        WordViewHolder.id.setText(words.get(position).getId());

    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public static class WordViewHolder extends RecyclerView.ViewHolder {
        private CardView cv;
        private static TextView firstName;
        private static TextView transcription;
        private static TextView translation;
        private static TextView gender;
        private static TextView id;

        WordViewHolder(View itemView) {
            super(itemView);


            cv = (CardView) itemView.findViewById(R.id.cv);

            firstName = (TextView) itemView.findViewById(R.id.tv_primary_phrases_card);
            translation = (TextView) itemView.findViewById(R.id.tv_card_Translation);
            transcription = (TextView) itemView.findViewById(R.id.tv_card_Transcription);
            gender = (TextView) itemView.findViewById(R.id.tv_card_Gender);
            id = (TextView) itemView.findViewById(R.id.tv_card_id);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Context context = v.getContext();
                        id = (TextView) v.findViewById(R.id.tv_card_id);
                        Intent intent = new Intent(context, ViewWord_activity.class);
                        intent.putExtra("selected_ID", id.getText().toString());
                        context.startActivity(intent);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });
        }

    }
}
