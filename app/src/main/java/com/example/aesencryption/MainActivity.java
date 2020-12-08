package com.example.aesencryption;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

        int veprimi = 0;
        static String keyGenerationParam = "MasterFiek2020" ;
        static String initializationVector = "2019202020212022";
        static String cypherInstance = "AES/CBC/PKCS5Padding";
        static String secretKeyInstance = "PBKDF2WithHmacSHA1";
        static int pswdIterations = 10;
        static  int keySize = 256;

        private ClipboardManager clipboardManager;
        private ClipData clipData;
        EditText tekstiHyres;
        TextView tekstiDales;
        Button btnVeprimi;
        Button btnKopjo;
        Button btnReset;
        EditText txtCelesi;
        private AppCompatCheckBox shfaqFsheh;

        @SuppressLint("WrongViewCast")
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                Switch ndryshuesiV = findViewById(R.id.ndryshuesiVeprimit);

                tekstiHyres = findViewById(R.id.tekstiHyres);
                tekstiDales = findViewById(R.id.txtOutput);
                btnVeprimi = findViewById(R.id.btnVeprimi);
                btnKopjo = findViewById(R.id.btnKopjo);
                txtCelesi = findViewById(R.id.txtCelesi);
                btnReset = findViewById(R.id.btnReset);
                shfaqFsheh = (AppCompatCheckBox) findViewById(R.id.shfaqFsheh);

                tekstiHyres.setPadding(20, 10, 20, 10);
                tekstiDales.setPadding(20, 10, 20, 10);
                txtCelesi.setPadding(20, 0, 20, 0);

                clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);

                ndryshuesiV.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (veprimi == 0) {
                                veprimi++;
                                btnVeprimi.setText("Dekripto");

                        } else {
                                veprimi--;
                                btnVeprimi.setText("Enkripto");

                        }
                });

                shfaqFsheh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                                if (isChecked) {
                                        // show password
                                        txtCelesi.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                } else {
                                        // hide password
                                        txtCelesi.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                }
                        }
                });

                btnKopjo.setOnClickListener(new View.OnClickListener()
                {
                        @Override
                        public void onClick(View v)
                        {
                                if (tekstiDales.getText().toString().trim().isEmpty() ||tekstiDales.getText().toString().equals(" ") || tekstiDales.getText().toString().length() ==0)
                                {
                                        Toast.makeText(MainActivity.this, "Asgjë për të kopjuar!", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                        String txtcopy = tekstiDales.getText().toString();
                                        clipData = ClipData.newPlainText("Teksti", txtcopy);
                                        clipboardManager.setPrimaryClip(clipData);
                                        Toast.makeText(MainActivity.this, "Teksti u kopjua!", Toast.LENGTH_SHORT).show();
                                }
                        }
                });

                btnReset.setOnClickListener(new View.OnClickListener()
                {
                        @Override
                        public void onClick(View v)
                        {
                                txtCelesi.setText(null);
                                tekstiDales.setText(null);
                                tekstiHyres.setText(null);
                        }
                });

                btnVeprimi.setOnClickListener(new View.OnClickListener()
                {
                        @Override
                        public void onClick(View v)
                        {
                                if(v==btnVeprimi)
                                {
                                        if(btnVeprimi.getText().equals("Enkripto"))
                                        {
                                                if (tekstiHyres.getText().toString().trim().isEmpty() ||	tekstiHyres.getText().toString().equals(" ") || tekstiHyres.getText().toString().length() ==0)
                                                {
                                                        Toast.makeText(MainActivity.this, "Ju lutem shenoni mesazhin hyrës!", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                        if (txtCelesi.getText().toString().trim().isEmpty() ||	txtCelesi.getText().toString().equals(" ") || txtCelesi.getText().toString().length() ==0)
                                                        {
                                                                Toast.makeText(MainActivity.this, "Ju lutem shenoni fjalëkalimin!", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else
                                                        {
                                                                try
                                                                {
                                                                        String tekstiNeHyrje = tekstiHyres.getText().toString().trim();
                                                                        String MesazhiEnkripruar = encrypt(tekstiNeHyrje, txtCelesi.getText().toString().trim());
                                                                        tekstiDales.setText(MesazhiEnkripruar);
                                                                }
                                                                catch (Exception e)
                                                                {
                                                                        e.printStackTrace();
                                                                }
                                                        }
                                                }

                                        }
                                        else
                                        {
                                                if (tekstiHyres.getText().toString().trim().isEmpty() ||	tekstiHyres.getText().toString().equals(" ") || tekstiHyres.getText().toString().length() ==0)
                                                {
                                                        Toast.makeText(MainActivity.this, "Ju lutem shenoni mesazhin hyrës!", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                        try
                                                        {
                                                                String tekstiNeHyrje = tekstiHyres.getText().toString().trim();
                                                                String mesazhiDekriptuar = decrypt(tekstiNeHyrje, txtCelesi.getText().toString().trim());
                                                                tekstiDales.setText(mesazhiDekriptuar);
                                                        }
                                                        catch (Exception e)
                                                        {
                                                                e.printStackTrace();
                                                        }
                                                }
                                        }
                                }
                        }
                });
        }

        public static String encrypt(String textToEncrypt, String celesi) throws Exception
        {
                SecretKeySpec skeySpec = new SecretKeySpec(getRaw(keyGenerationParam, celesi), "AES");
                Cipher cipher = Cipher.getInstance(cypherInstance);
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new     IvParameterSpec(initializationVector.getBytes()));
                byte[] encrypted = cipher.doFinal(textToEncrypt.getBytes());
                return android.util.Base64.encodeToString(encrypted, android.util.Base64.DEFAULT);
        }

        public static String decrypt(String textToDecrypt, String celesi) throws Exception
        {
                byte[] encryted_bytes = android.util.Base64.decode(textToDecrypt, android.util.Base64.DEFAULT);
                SecretKeySpec skeySpec = new SecretKeySpec(getRaw(keyGenerationParam, celesi), "AES");
                Cipher cipher = Cipher.getInstance(cypherInstance);
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(initializationVector.getBytes()));
                byte[] decrypted = cipher.doFinal(encryted_bytes);
                return new String(decrypted, "UTF-8");
        }

        // Gjenerimi i qelesit sekret
        private static byte[] getRaw(String keyGenerationParam, String salt)
        {
                try {
                        SecretKeyFactory factory = SecretKeyFactory.getInstance(secretKeyInstance);
                        KeySpec spec = new PBEKeySpec(keyGenerationParam.toCharArray(), salt.getBytes(), pswdIterations, keySize);
                        return factory.generateSecret(spec).getEncoded();
                }
                catch (InvalidKeySpecException e)
                {
                        e.printStackTrace();
                }
                catch (NoSuchAlgorithmException e)
                {
                        e.printStackTrace();
                }
                return new byte[0];
        }
}