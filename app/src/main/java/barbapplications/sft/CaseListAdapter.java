package barbapplications.sft;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class CaseListAdapter extends RecyclerView.Adapter<CaseListAdapter.ViewHolder> {
    private ArrayList<CaseID> caseIDS;
    private AdapterCallback listener;

    CaseListAdapter(ArrayList<CaseID> caseIDS, AdapterCallback listener){
        this.caseIDS = caseIDS;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.case_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.initView(caseIDS.get(position).getCid());
    }

    @Override
    public int getItemCount() {
        return caseIDS.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView caseItemTitle;
        EditText badgeEntry;
        ImageButton imageButton;
        AppCompatButton sendButton;
        int cid;

        ViewHolder(View itemView) {
            super(itemView);
        }

        void initView(final int caseNumber){
            cid = caseNumber;
            caseItemTitle = itemView.findViewById(R.id.caseItemTitle);
            caseItemTitle.setText(Integer.toString(caseNumber));
            badgeEntry = itemView.findViewById(R.id.badgeNumber);
            imageButton = itemView.findViewById(R.id.takePictures);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("ADAPTER", Integer.toString(cid));
                    listener.onImageButtonClicked(cid);
                }
            });

            sendButton = itemView.findViewById(R.id.sendButton);
            sendButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    listener.onSendButtonClicked(caseNumber, Integer.parseInt(badgeEntry.getText().toString()));
                }
            });
        }
    }

    public interface AdapterCallback{
        void onImageButtonClicked(int caseNumber);
        void onSendButtonClicked(int caseNumber, int badgeNumber);
    }
}
