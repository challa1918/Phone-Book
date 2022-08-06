package com.example.database.Model;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.database.Data.DatabaseHandler;
import com.example.database.MainActivity;
import com.example.database.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Collection;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {
    ArrayList<Contact> ContacList,allcontacts;
    Context context;
    DatabaseHandler databaseHandler;

    View card;
    int i = 0;
    int f=0;

    public RecyclerViewAdapter(ArrayList<Contact> contacList, Context context) {
        ContacList = contacList;
        this.allcontacts=new ArrayList<>(contacList);
        this.context = context;
        databaseHandler = new DatabaseHandler(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contactrow, parent, false);


        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {


        final Contact contact = ContacList.get(position);

        holder.name.setText(contact.getName());
        holder.phn.setText(contact.getPhnno());
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                androidx.appcompat.app.AlertDialog dialog;
                androidx.appcompat.app.AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setMessage("Are you sure do you want to delete contact ?");

                alertDialog.setIcon(R.drawable.delt);
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHandler.delete(contact);
                        allcontacts.remove(allcontacts.indexOf(contact));
                        notifyDataSetChanged();
                        ContacList=databaseHandler.GetContacts();


                        Toast.makeText(context, "Deleted ", Toast.LENGTH_LONG).show();

                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog= alertDialog.create();
                dialog.show();


                    //    dialog.cancel();


            }

        });
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context,"Calling...",Toast.LENGTH_LONG).show();
                Intent call = new Intent(Intent.ACTION_CALL);
                call.setData(Uri.parse("tel:"+contact.phnno.trim()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    int permission=1;
                    ActivityCompat.requestPermissions((Activity)context,new String[]{Manifest.permission.CALL_PHONE},permission);



                    return;
                }
                context.startActivity(call);
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {


                Intent share= new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                      share.putExtra(Intent.EXTRA_TEXT,"Name : "+contact.name+"\n"+"Phnno : "+contact.phnno);
                        context.startActivity(Intent.createChooser(share,"Select  the app to share contact"));
                ;
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = LayoutInflater.from(context).inflate(R.layout.popup, null);
                final TextView uname = view.findViewById(R.id.popname);
                final TextView uphn = view.findViewById(R.id.popphnno);
                Button ub = view.findViewById(R.id.popaddb);
                ub.setText("Update Contact");
                uname.setText(contact.getName());
                uphn.setText(contact.getPhnno());
                final Dialog d = new Dialog(context);
                d.setContentView(view);
                d.show();
                final Contact updatedcontact = new Contact();

                ub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int pos=allcontacts.indexOf(contact);
                        updatedcontact.setName(uname.getText().toString());
                        updatedcontact.setPhnno(uphn.getText().toString());
                        updatedcontact.setid(contact.getid());
                        databaseHandler.update(updatedcontact);
                        ContacList=databaseHandler.GetContacts();
                        allcontacts.remove(pos);
                        allcontacts.add(updatedcontact);

notifyDataSetChanged();
                        Toast.makeText(context, "Edited", Toast.LENGTH_LONG).show();
                        System.out.println("Updated Contact: " + updatedcontact.toString());
                        System.out.println("Previous contact: " + contact.toString());
                        d.cancel();
                    }
                });




            }
        });




    }


    @Override
    public int getItemCount() {


        System.out.println(++i+" : "+ContacList.size());


        return ContacList.size();
    }


    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Contact> registerDataArrayList= new ArrayList<>();
            if(constraint.toString().isEmpty()){
                registerDataArrayList.addAll(allcontacts);

            }else {
                for (Contact rd:
                        allcontacts) {
                    if(rd.name.toLowerCase().contains(constraint.toString().toLowerCase())){
                        registerDataArrayList.add(rd);
                    }

                }

            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=registerDataArrayList;
            return filterResults;


        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ContacList.clear();
            ContacList.addAll((Collection<? extends Contact>) results.values);
            notifyDataSetChanged();
        }

    };






    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView phn;
        ImageButton del, edit,call,share;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.Cardname);
            phn = itemView.findViewById(R.id.cardphn);
            del = itemView.findViewById(R.id.del);
            edit = itemView.findViewById(R.id.edit);
            call=itemView.findViewById(R.id.call);
            share=itemView.findViewById(R.id.share);




        }
    }
}






