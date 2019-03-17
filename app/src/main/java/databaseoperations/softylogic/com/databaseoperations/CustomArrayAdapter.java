package databaseoperations.softylogic.com.databaseoperations;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomArrayAdapter extends BaseAdapter {





    Context mContext;
    ArrayList<userDetails> userArrayList;

    TextView txtName;
    TextView txtContact;
    TextView txtuserType;
    TextView txtemailId;
    TextView txtRegDate;
    ImageButton del;
    ImageButton edit;
    DbHelper db;

    SparseBooleanArray mSparseBooleanArray;

    public CustomArrayAdapter(Context mContext, ArrayList<userDetails> users) {
        mSparseBooleanArray = new SparseBooleanArray();
        this.mContext = mContext;
        this.userArrayList = users;
        db = new DbHelper(mContext);
    }


    @Override
    public int getCount() {
        return userArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return userArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {




            convertView = View.inflate(mContext, R.layout.listview_row_item, null);

            txtContact = convertView.findViewById(R.id.contact);
            txtName = convertView.findViewById(R.id.nameTxtView1);
        txtemailId = convertView.findViewById(R.id.emailID);
        txtuserType = convertView.findViewById(R.id.userType);
        txtRegDate = convertView.findViewById(R.id.regDate);
        del = convertView.findViewById(R.id.delbtn);
        edit = convertView.findViewById(R.id.editbtn);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDetails userdel;
                userdel = userArrayList.get(position);
                db.deleteUser(userdel);
                Toast.makeText(mContext, "Entry Deleted", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final userDetails useredit;
                useredit = userArrayList.get(position);


                LayoutInflater lit = LayoutInflater.from(mContext);
                View prompt2 = lit.inflate(R.layout.adduserdialog ,null);
                AlertDialog.Builder alertDialogBuilderadd = new AlertDialog.Builder(mContext);
                alertDialogBuilderadd.setView(prompt2);
                final EditText username = prompt2.findViewById(R.id.addname);
                final EditText password =  prompt2.findViewById(R.id.addpassword);
                final EditText useremail =  prompt2.findViewById(R.id.addemail);
                final EditText contact =  prompt2.findViewById(R.id.addcontact);
                final EditText usertype =  prompt2.findViewById(R.id.addUserType);
                final EditText regDate = prompt2.findViewById(R.id.addregDate);

                username.setText(useredit.name);
                password.setText(useredit.password);
                useremail.setText(useredit.emailid);
                contact.setText(useredit.contact);
                usertype.setText(Integer.toString(useredit.usertype));

                regDate.setText(useredit.registerationdate);
                alertDialogBuilderadd.setTitle("Update user");
                alertDialogBuilderadd.setCancelable(false)
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {

                                useredit.name = username.getText().toString();
                                useredit.password = password.getText().toString();
                                useredit.emailid = useremail.getText().toString();
                                useredit.contact = contact.getText().toString();
                                useredit.usertype = Integer.parseInt(usertype.getText().toString());
                                useredit.modifydate = new CommonMethods().getDate();
                               db.updateUser(useredit);

                                    Toast.makeText(mContext,"Data Updated" , Toast.LENGTH_SHORT ).show();
                                    notifyDataSetChanged();


                            }

                        });

                alertDialogBuilderadd.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();


                    }
                });

                alertDialogBuilderadd.show();

            }
        });
            




        txtContact.setText(userArrayList.get(position).contact);
        txtName.setText(userArrayList.get(position).name);
        txtemailId.setText(userArrayList.get(position).emailid);
        if(userArrayList.get(position).usertype == 1){

            txtuserType.setText("Normal User");
        }
        if(userArrayList.get(position).usertype == 2){

            txtuserType.setText("Advanced User");
        }
        txtRegDate.setText(userArrayList.get(position).registerationdate);
        convertView.setTag(userArrayList.get(position).name);
        return convertView;
    }


}