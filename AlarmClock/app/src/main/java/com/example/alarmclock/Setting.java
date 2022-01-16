package com.example.alarmclock;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class Setting extends Fragment {
    public static int RESULT_PICK_IMAGEFILE = 1001;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        return (ViewGroup) inflater.inflate(R.layout.setting, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onStart();

        ImageView bar_back_btn = getActivity().findViewById(R.id.bar_back_button);
        bar_back_btn.setVisibility(View.VISIBLE);

        Button changebg = view.findViewById(R.id.changebg);
        changebg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent, RESULT_PICK_IMAGEFILE);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData)
    {
        if (requestCode == RESULT_PICK_IMAGEFILE && resultCode == Activity.RESULT_OK)
        {
            if (resultData.getData() != null)
            {
                Uri uri = resultData.getData();
                try {
                    saveImg(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                String uriPath = uri.toString();
//                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Info", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("uriPath",uriPath);
//                editor.apply();
            }
        }
    }

    public void saveImg(Uri uri) throws IOException {
        FileOutputStream out = null;
        ImageDecoder.Source imgsource;
        Bitmap imgbitmap = null;
        ContentResolver contentResolver = requireActivity().getContentResolver();
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                imgsource = ImageDecoder.createSource(contentResolver, uri);
                imgbitmap = ImageDecoder.decodeBitmap(imgsource);
            }else
            {
                MediaStore.Images.Media.getBitmap(contentResolver, uri);
            }
            out = requireActivity().openFileOutput("backimg.png",Context.MODE_PRIVATE);
            if (imgbitmap != null)
            {
                imgbitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            }

        }catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }catch (IOException e)
        {
            e.printStackTrace();
        }finally {
            if (out != null)
            {
                out.close();
            }
        }
    }

}
