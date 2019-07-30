package com.example.sandhyayalla.firebaseexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ChatRoomActivity extends AppCompatActivity {

    TextView tv_name;
    ImageView iv_logout;
    ImageView iv_sendmessage;
    ImageView iv_addimage;
    EditText et_message;
   // String downloadlink="";
    User user;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Messages");
    ArrayList<Message> messagelist=new ArrayList<>();
    ListView lv_msglist;
    String key;
    MessageAdapter adapter;
    String imageurl="";
    int selected =0;
    FirebaseAuth firebaseAuth;
    //Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        iv_addimage=(ImageView)findViewById(R.id.iv_addimage);
        iv_sendmessage=(ImageView)findViewById(R.id.iv_sendmessage);
        et_message=(EditText)findViewById(R.id.et_entertext);
        lv_msglist=(ListView)findViewById(R.id.listview);
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null)
        {
            finish();
            Intent i=new Intent(ChatRoomActivity.this,MainActivity.class);
            startActivity(i);
        }
        if(getIntent()!=null && getIntent().getExtras()!=null)
        {

             user=(User)getIntent().getExtras().getSerializable("user");
             Log.d("demo","user in chat "+user.toString());
            tv_name=(TextView)findViewById(R.id.tv_name);
            if(user!=null) {
                tv_name.setText(user.username+" "+user.lastname);
            }

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messagelist.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    messagelist.add(message);
                    adapter = new MessageAdapter(ChatRoomActivity.this, R.layout.select_item, messagelist);
                    lv_msglist.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        }

        iv_logout=(ImageView)findViewById(R.id.iv_logout);
        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                FirebaseAuth.getInstance().signOut();
                Intent i =new Intent(ChatRoomActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

        iv_addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add image logic
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),100);
            }
        });

        iv_sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_message.getText().toString().isEmpty())
                {
                    et_message.setError("Type message to send ");
                    //Toast.makeText(ChatRoomActivity.this, "", Toast.LENGTH_SHORT).show();
                }
                else {
                    //ImageView imageView=(ImageView)findViewById(R.id.imageView);
                    if (selected == 1)
                    {

                        iv_addimage.setDrawingCacheEnabled(true);
                    iv_addimage.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) iv_addimage.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] data = baos.toByteArray();
                    String path = "fireimage/" + UUID.randomUUID() + ".png";
                    // Create a storage reference from our app

                    final StorageReference storageRef1 = storage.getReference(path);
                    //StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("text", et_message.getText().toString()).build();
                    UploadTask uploadTask = storageRef1.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("demo", "not able to upload pic" + e);
                        }
                    }).addOnSuccessListener(ChatRoomActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageRef1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadlink = uri.toString();
                                    imageurl = downloadlink;
                                    Log.d("demo", "downloadlink " + downloadlink);

                                    //Message object creation
                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String formattedDate = df.format(c.getTime());
                                    key = myRef.push().getKey();
                                    Log.d("demo", "while storing " + imageurl.toString());
                                    Message message = new Message(et_message.getText().toString(), formattedDate, user.username, user.lastname, imageurl, key);
                                    Log.d("demo", "Messages ius " + message.toString());
                                    myRef.child(key).setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(ChatRoomActivity.this, "Message sent !", Toast.LENGTH_SHORT).show();
                                            iv_addimage.setImageResource(R.drawable.addimage);
                                            imageurl = "";
                                            et_message.setText("");
                                            selected=0;
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ChatRoomActivity.this, "Not able to send the message", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });


                        }
                    });
                }
                else
                    {
                        //Message object creation
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = df.format(c.getTime());
                        key = myRef.push().getKey();
                        Log.d("demo","username is "+user.username);
                        Message message = new Message(et_message.getText().toString(), formattedDate, user.username, user.lastname, imageurl, key);
                        Log.d("demo", "Messages ius " + message.toString());
                        myRef.child(key).setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ChatRoomActivity.this, "Message sent !", Toast.LENGTH_SHORT).show();
                                imageurl = "";
                                et_message.setText("");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ChatRoomActivity.this, "Not able to send the message", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }



                }
            }
        });

        //delete message
       /* myRef.removeEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messagelist.clear();
                Log.d("demo","removeevent");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message task = snapshot.getValue(Message.class);
                    messagelist.add(task);

                }

                adapter = new MessageAdapter(ChatRoomActivity.this, R.layout.select_item, messagelist);
                lv_msglist.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */


    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(ChatRoomActivity.this.getContentResolver(), data.getData());
                        iv_addimage.setImageBitmap(bitmap);
                        selected=1;
                        Toast.makeText(this, "selected image successfully", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                selected=0;
                Toast.makeText(ChatRoomActivity.this, "cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public class MessageAdapter extends ArrayAdapter<Message> {

        public MessageAdapter(@NonNull Context context, int resource, @NonNull List<Message> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

             Message message=getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.select_item, parent, false);
            }
            TextView tv_text = (TextView) convertView.findViewById(R.id.firstName);
            tv_text.setText(message.firstname.toString());
            TextView tv_message = (TextView) convertView.findViewById(R.id.messageText);
            tv_message.setText(message.message.toString());

            TextView tv_time = (TextView) convertView.findViewById(R.id.time);
            String formattedtime = formatdate(message.time);
            tv_time.setText(formattedtime);
            final ImageView iv_delete=(ImageView)convertView.findViewById(R.id.deleteIcon);
            ImageView iv_imagedownlod=(ImageView)convertView.findViewById(R.id.imageView);
            iv_delete.setTag(position);
           /* iv_delete.setVisibility(View.INVISIBLE);

                    if(user.username.equals(message.firstname))
                    {
                        if(user.lastname.equals(message.lastname))
                        {
                            iv_delete.setVisibility(View.VISIBLE);
                        }
                    }*/

            iv_imagedownlod.setVisibility(View.VISIBLE);
            if( !message.downloadablelink.isEmpty()) {
                Picasso.get().load(message.downloadablelink).into(iv_imagedownlod);
            }
            else if(message.downloadablelink.isEmpty())
            {
                iv_imagedownlod.setVisibility(View.INVISIBLE);
            }


            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i=(int)iv_delete.getTag();
                    Message task = messagelist.get(i);
                    Log.d("demo","tasktodelte"+task.toString());
                    myRef.child(task.key).removeValue();
                    messagelist.remove(i);
                    adapter = new MessageAdapter(ChatRoomActivity.this, R.layout.select_item, messagelist);
                    lv_msglist.setAdapter(adapter);

                   // adapter.notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }
    //format date
    public static String formatdate(String dateString) {

        // String dateString="2015-09-25 15:00:47";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

        Date convertedDate = new Date();
        //TimeZone estTime = TimeZone.getTimeZone(convertedDate);

        try {

            convertedDate = dateFormat.parse(dateString);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Locale locale=Locale.US;

        Calendar cal = Calendar.getInstance();
        cal.setTime(convertedDate);
        cal.add(Calendar.HOUR, -4);  // API response is in GMT hence converting it to EDT

        Date actualDate = cal.getTime();

        PrettyTime p = new PrettyTime();

        String datetime = p.format(actualDate);
        //Log.d("demo", "created" + datetime);


        return datetime;
    }
    //end format date
}
