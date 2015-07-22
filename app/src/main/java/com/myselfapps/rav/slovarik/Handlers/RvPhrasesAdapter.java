package com.myselfapps.rav.slovarik.Handlers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myselfapps.rav.slovarik.Objects.Phrase;
import com.myselfapps.rav.slovarik.R;
import com.myselfapps.rav.slovarik.ViewPhrase_activity;

import java.util.List;

public class RvPhrasesAdapter extends RecyclerView.Adapter<RvPhrasesAdapter.PhraseViewHolder> {
    private List<Phrase> phrases;
    public RvPhrasesAdapter(List<Phrase> phrases) {
        this.phrases = phrases;
    }

    @Override
    public PhraseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new PhraseViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.phrase_card_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(PhraseViewHolder holder, int position) {
        PhraseViewHolder.primary.setText(phrases.get(position).getId()+" - "+phrases.get(position).getPrimary());//Убрать по завершении
        PhraseViewHolder.translation.setText(phrases.get(position).getSecondary());
        PhraseViewHolder.transcription.setText("[" + phrases.get(position).getTranscription() + "]");
        PhraseViewHolder.category.setText(phrases.get(position).getCategory());
        PhraseViewHolder.id.setText(phrases.get(position).getId());

    }

    @Override
    public int getItemCount() {
        return phrases.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public static class PhraseViewHolder extends RecyclerView.ViewHolder {
        private CardView cv;
        private static TextView primary;
        private static TextView transcription;
        private static TextView translation;
        private static TextView category;
        private static TextView id;

        PhraseViewHolder(View itemView) {
            super(itemView);


            cv = (CardView) itemView.findViewById(R.id.cv);

            primary = (TextView) itemView.findViewById(R.id.tv_primary_phrases_card);
            translation = (TextView) itemView.findViewById(R.id.tv_translation_phrases_card);
            transcription = (TextView) itemView.findViewById(R.id.tv_transcription_phrases_card);
            category = (TextView) itemView.findViewById(R.id.tv_category_phrases_card);
            id = (TextView) itemView.findViewById(R.id.tv_id_phrases_card);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Context context = v.getContext();
                        id = (TextView) v.findViewById(R.id.tv_id_phrases_card);
                        Intent intent = new Intent(context, ViewPhrase_activity.class);
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


