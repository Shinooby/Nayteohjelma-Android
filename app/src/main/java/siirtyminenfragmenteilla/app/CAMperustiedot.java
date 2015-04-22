package siirtyminenfragmenteilla.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;

import org.apache.http.conn.scheme.PlainSocketFactory;

import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import siirtyminenfragmenteilla.app.adapterit.MySSLSocketFactory;
import siirtyminenfragmenteilla.app.adapterit.customListAdapterCameraItems;
import siirtyminenfragmenteilla.app.adapterit.AppController;
import siirtyminenfragmenteilla.app.apuluokat.ImageAndText;
import siirtyminenfragmenteilla.app.apuluokat.Item;
import siirtyminenfragmenteilla.app.apuluokat.MyApp;
import siirtyminenfragmenteilla.app.adapterit.MySingleton;
import siirtyminenfragmenteilla.app.apuluokat.customListForNewPics;


public class CAMperustiedot extends Activity implements AdapterView.OnItemClickListener {

    private Button kuvanLisäys;
    private Button kuvanLisäysGalleriasta;

    private ImageView dialoginKuva;

    private String receivedItemID;
    private String itemIDforQuery;
    private String desiredAttributeForUpdate;

    Bitmap bmImg;
    MyApp app = new MyApp();

    ImageLoader mImageLoader;
    ImageLoader mImageLoaderForTest;

    private NetworkImageView dialoginKuvaMuuttamistaVarten;
    //Login "tägi"
    private static final String TAG = CAMperustiedot.class.getSimpleName();

    private String pathbeginning = app.getJSONURLForImages();

    private AlertDialog dialogForModification;

    private ListView listviewForCameraitems;
    private List<Item> listOfItems = new ArrayList<Item>();
    private customListAdapterCameraItems adapter;

    private static final int IMAGE_PICKER_SELECT = 999;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private List<ImageAndText> listOfImageAndText;
    private JSONArray ArrayToReceive;
    private ListView listViewJossaOnUusiaKuvia;
    private TextView otsikkoUusienKuvienListViewlle;

    private customListForNewPics adapterForNewPics;
    private String descForNewPic;

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }
    Bitmap bm;

    public static File file;

    private static final String PREFS = "prefs";
    //talletettavat tiedot
    public static final String PREF_NAME = "name";
    public static final String PREF_PASSWORD = "password";
    public static final String PREF_FID = "fid";
    public SharedPreferences mSharedPreferences;

    private int listItemToModify;
    private String newDescForListItem;
    private String pictureToDelete;

    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cam_perustiedot);

        // Luotetaan SSLCertifikaatteihin -- Pitää tehdä jos haluaa kuvat Volleyn kautta.
        NukeSSLCerts.nuke();

        // Haetaan imageLoader singletonin kautta
         mImageLoaderForTest = MySingleton.getInstance(this).getImageLoader();

        setCredentialsFromPreferences();

        listViewJossaOnUusiaKuvia = (ListView) findViewById(R.id.listViewUusilleKuville);
        otsikkoUusienKuvienListViewlle = (TextView) findViewById(R.id.textViewUusilleKuville);

        listOfImageAndText = new ArrayList<ImageAndText>();
        adapterForNewPics = new customListForNewPics(this, listOfImageAndText);
        listViewJossaOnUusiaKuvia.setAdapter(adapterForNewPics);

        mImageLoader = AppController.getInstance().getImageLoader();
        listviewForCameraitems = (ListView) findViewById(R.id.listViewCAMperustiedot);
        adapter = new customListAdapterCameraItems(this, listOfItems);

        listviewForCameraitems.setAdapter(adapter);

        kuvanLisäys = (Button) findViewById(R.id.addNewPictureAndDesc);
        kuvanLisäysGalleriasta = (Button) findViewById(R.id.addNewPictureFromGallery);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                receivedItemID = "";
            } else {
                receivedItemID = extras.getString("ID_TO_GET");
                try {
                    String arrayAsString = extras.getString("jsonarray");
                    ArrayToReceive = new JSONArray(arrayAsString);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Ei onnistunut.", "Array on tyhjä");
                    ArrayToReceive = null;
                }
            }
        }               // Parsitaan minkä alasivun kautta tultiin kamerasivulle
        if (receivedItemID.equals("pageID1")) {
            setTitle("Koneet");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Perustiedot
        }
        if (receivedItemID.equals("pageID2")) {
            setTitle("Tuotantorakennukset");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Perustiedot
        }
        if (receivedItemID.equals("pageID3")) {
            setTitle("Tuotantorakennusten tekniikka");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Perustiedot
        }
        if (receivedItemID.equals("pageID4")) {
            setTitle("Viljeltävät kasvit");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Perustiedot
        }
        if (receivedItemID.equals("pageID4")) {
            setTitle("Rakennusprojektit");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Perustiedot
        }
        if (receivedItemID.equals("pageID5")) {
            setTitle("Työturvallisuus");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Perustiedot
        }
        if (receivedItemID.equals("pageID6")) {
            setTitle("Perusmuokkaus");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Kasvituotanto
        }
        if (receivedItemID.equals("pageID7")) {
            setTitle("Kylvömuokkaus");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Kasvituotanto
        }
        if (receivedItemID.equals("pageID8")) {
            setTitle("Lannoitus");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Kasvituotanto
        }
        if (receivedItemID.equals("pageID8")) {
            setTitle("Kylvö");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Kasvituotanto
        }
        if (receivedItemID.equals("pageID9")) {
            setTitle("Rikkakasvit");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Kasvituotanto --> Kasvinsuojelu
        }
        if (receivedItemID.equals("pageID10")) {
            setTitle("Kasvitaudit");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Kasvituotanto --> Kasvinsuojelu
        }
        if (receivedItemID.equals("pageID11")) {
            setTitle("Tuholaiset");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Kasvituotanto --> Kasvinsuojelu
        }
        if (receivedItemID.equals("pageID12")) {
            setTitle("Kasvusääteet");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Kasvituotanto --> Kasvinsuojelu
        }
        if (receivedItemID.equals("pageID13")) {
            setTitle("Ruiskutusten onnistuminen");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Kasvituotanto --> Kasvinsuojelu
        }
        if (receivedItemID.equals("pageID14")) {
            setTitle("Viljasadon korjuu");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Kasvituotanto
        }
        if (receivedItemID.equals("pageID15")) {
            setTitle("Nurmisadon korjuu");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Kasvituotanto
        }
        if (receivedItemID.equals("pageID16")) {
            setTitle("Sadon jatkokäsittely");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Kasvituotanto
        }
        if (receivedItemID.equals("pageID17")) {
            setTitle("Kuvia päivärutiineista");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Kotieläimet - Tuotantokierto
        }
        if (receivedItemID.equals("pageID18")) {
            setTitle("Rodut");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Kotieläimet - Kotieläinjalostus
        }
        if (receivedItemID.equals("pageID19")) {
            setTitle("Erityiset tapahtumat");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Kotieläimet
        }
        if (receivedItemID.equals("pageID20")) {
            setTitle("Lypsyyn liittyvät tapahtumat");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Kotieläimet
        }
        if (receivedItemID.equals("pageID21")) {
            setTitle("Metsälön kehitysluokat");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Metsätalous - Metsälön perustiedot
        }
        if (receivedItemID.equals("pageID22")) {
            setTitle("Taimikon hoito");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Metsätalous
        }
        if (receivedItemID.equals("pageID23")) {
            setTitle("Kasvatushakkuut");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Metsätalous
        }
        if (receivedItemID.equals("pageID24")) {
            setTitle("Metsälön uudistaminen");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Metsätalous
        }
        if (receivedItemID.equals("pageID25")) {
            setTitle("Metsälön terveys");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Metsätalous
        }
        if (receivedItemID.equals("pageID26")) {
            setTitle("Metsätalouden koneet ja kalusto");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Metsätalous
        }
        if (receivedItemID.equals("pageID27")) {
            setTitle("Metsälön tuotteiden käyttö");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Metsätalous - Metsälön talous
        }
        if (receivedItemID.equals("pageID28")) {
            setTitle("Tehdyt metsänhoitotyöt");
            itemIDforQuery = receivedItemID;
            desiredAttributeForUpdate = "individualAttribute";
            // Metsätalous
        }
        else {
            setTitle("Kamerasivu");
            itemIDforQuery = "ID";
            desiredAttributeForUpdate = "attribute";
        }

        String emptyUrl = "http://123.12.123.12/deliberateBadUrl";

        if (ArrayToReceive != null) {
            if (ArrayToReceive.length() != 0) {
                StringRequest itemReq = new  StringRequest(emptyUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG, response.toString());

                                for (int i = 0; i < ArrayToReceive.length(); i++) {
                                    try {
                                        JSONObject obj = ArrayToReceive.getJSONObject(i);
                                        Item item = new Item();
                                        String itemID = obj.getString("itemID");
                                        item.setItemID(itemID);
                                        item.setItemInfo(obj.getString("itemInfo"));

                                        String imageUrl = pathbeginning + "?ID=" + itemID +
                                                "&LargestSize=150&ImageType=" + itemIDforQuery;
                                        item.setThumbnailUrl(imageUrl);
                                        listOfItems.add(item);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                });
                AppController.getInstance().addToRequestQueue(itemReq);
            }
            else {
                Log.d("Listview:n täyttö", "Ei tehty, koska Arrayn pituus on 0");
            }
        }

        listviewForCameraitems.setOnItemClickListener(this);

        kuvanLisäys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            callDialogForNewPicture();
            }
        });

        kuvanLisäysGalleriasta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                callDialogForNewPictureFromGallery();
            }
        });
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {

        Log.d("omg android", position + ": " + listviewForCameraitems.getSelectedItem());
        Log.d("omg android", "itemID: " + listOfItems.get(position).getItemID());

         listItemToModify = position;

        String itemIDfromList = listOfItems.get(position).getItemID();
        String itemInfoFromList = listOfItems.get(position).getItemInfo();
        // Thumbnail-urlia ei tarvita koska haemme uuden kuvan itemID:n ja kyseessä olevan aihealueen perusteella

        callDialogToModifyListItem(itemIDfromList, itemInfoFromList);
    }

    private void callDialogToModifyListItem(final String itemIDfromList, final String itemInfoFromList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialgo_modifycameraitem, null);

        dialoginKuvaMuuttamistaVarten = (NetworkImageView) view.findViewById(R.id.networkImageViewSuurempaaKuvaaVarten);
        final EditText kuvaus = (EditText) view.findViewById(R.id.dialogin_kuvauksen_editText);

        Button uusiKuvaKameralla = (Button) view.findViewById(R.id.dialogButton_kuvaKameralla);
        Button uusiKuvaGalleriasta = (Button) view.findViewById(R.id.dialogButton_kuvaGalleriasta);
        Button kuvanPoistaminen = (Button) view.findViewById(R.id.kuvanPoistamisNappi);

        uusiKuvaGalleriasta.setVisibility(View.GONE);
        uusiKuvaKameralla.setVisibility(View.GONE);

        uusiKuvaKameralla.setOnClickListener(onClickListenerForModifyItems);
        uusiKuvaGalleriasta.setOnClickListener(onClickListenerForModifyItems);
        kuvanPoistaminen.setOnClickListener(onClickListenerForModifyItems);

        kuvaus.setText(itemInfoFromList);

        pictureToDelete = itemIDfromList;

        String urlForLargerImage = pathbeginning + "?ID=" + itemIDfromList + "&LargestSize=300&ImageType=" + itemIDforQuery;
        dialoginKuvaMuuttamistaVarten.setImageUrl(urlForLargerImage, mImageLoader);

        builder.setView(view)
                        // Add action buttons
                .setPositiveButton("Tallenna", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("Saadaanko tekstit?", "TEST");
                        String newInfo = kuvaus.getText().toString();
                        if (!newInfo.equals(itemInfoFromList)) {
                            if (newInfo.equals("")) {
                                newInfo = "Kuvaus puuttuu";
                                newDescForListItem = newInfo;
                                new JSONUpdatePRImageItem().execute(itemIDfromList, newInfo);
                            }
                            if (!newInfo.matches("[a-zA-Z0-9.,?: ]*")) {
                                Toast.makeText(getApplicationContext(), "Tarkista ettei viestisi sisällä seuraavia \n " +
                                        "erikoismerkkejä: \"  \' ;", Toast.LENGTH_LONG).show();
                            } else {
                                newDescForListItem = newInfo;
                                new JSONUpdatePRImageItem().execute(itemIDfromList, newInfo);
                            }
                        }
                    }
                })
                .setNegativeButton("Peruuta", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Älä tee mitään
                    }
                });
        dialogForModification = builder.create();
        dialogForModification.show();

    }

    View.OnClickListener onClickListenerForModifyItems = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.kuvanPoistamisNappi:
                    Log.d("Saadaanko tekstit?", "TEST");

                        // Tehdään uusi alertDialog, joka kysyy varmistusta kuvan poistolle.
                    final AlertDialog alert;
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CAMperustiedot.this);

                    dialogBuilder.setTitle("Kuvan poistaminen");
                    dialogBuilder.setMessage("Haluatko varmasti poistaa kuvan?");
                    dialogBuilder.setPositiveButton("Kyllä", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Painettiin kyllä nappia--> Kutsutaan AsyncTaskia poistamaan kuva

                            new JSONRemovePRImageItem().execute();
                        }
                    });
                    dialogBuilder.setNegativeButton("Peruuta", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Painettiin Peruuta-nappia --> Ei tehdä mitään.
                        }
                    });

                    AlertDialog dialog = dialogBuilder.create();
                    dialog.show();
                    break;
            }
        }
    };

    private void callDialogForNewPictureFromGallery() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View view = inflater.inflate(R.layout.custom_dialog, null);

        dialoginKuva = (ImageView) view.findViewById(R.id.kuvaJaSenOttoImageview);
        final EditText kuvaus = (EditText) view.findViewById(R.id.dialogin_kuvauksen_editText);

        dialoginKuva.setOnClickListener(onClickListenerForPictureFromGallery);


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Tallenna", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {


                         bm = ((BitmapDrawable) dialoginKuva.getDrawable()).getBitmap();
                        // Saatiin bitmap irti
                        setBm(bm);
                        String value = kuvaus.getText().toString();
                        if (value.equals("")) {
                            value = "Kuvaus puuttuu";
                        }
                        if (!value.matches("[a-zA-ZÄ-Öä-öåÅ0-9/?.,: _-]*")) {
                            Toast.makeText(getApplicationContext(), "Tarkista ettei viestisi sisällä seuraavia \n " +
                                    "erikoismerkkejä: \"  \' ;", Toast.LENGTH_LONG).show();
                        } else {
                            new JSONSendImageAndInfo().execute(receivedItemID, value);
                        }
                    }
                })
                .setNegativeButton("Peruuta", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Älä tee mitään
                    }
                }).create().show();
    }


    private void callDialogForNewPicture() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        View view = inflater.inflate(R.layout.custom_dialog, null);

        dialoginKuva = (ImageView) view.findViewById(R.id.kuvaJaSenOttoImageview);
        final EditText kuvaus = (EditText) view.findViewById(R.id.dialogin_kuvauksen_editText);

        dialoginKuva.setOnClickListener(onClickListenerForPicture);


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                        // Add action buttons
                .setPositiveButton("Tallenna", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton("Peruuta", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Älä tee mitään
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean wantToCloseDialog = false;

                Log.d("Saadaanko tekstit?", "");
                bm = ((BitmapDrawable) dialoginKuva.getDrawable()).getBitmap();

                // Saatiin bitmap irti
                setBm(bm);
                String descReceived = kuvaus.getText().toString();
                if (descReceived.equals("")) {
                    descReceived = "Kuvaus puuttuu";
                    new JSONSendImageAndInfo().execute(receivedItemID, descReceived);
                    wantToCloseDialog = true;
                } if (!descReceived.matches("[a-zA-Z0-9.,?: ]*")) {
                    Toast.makeText(getApplicationContext(), "Tarkista ettei viestisi sisällä seuraavia \n " +
                            "erikoismerkkejä: \"  \' ;", Toast.LENGTH_LONG).show();
                }
                else {
                    new JSONSendImageAndInfo().execute(receivedItemID, descReceived);
                    wantToCloseDialog = true;
                }

                if (wantToCloseDialog)
                     dialog.dismiss();
            }
        });


    }

    private View.OnClickListener onClickListenerForPictureFromGallery = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, IMAGE_PICKER_SELECT);
        }
    };

    private View.OnClickListener onClickListenerForPicture = new View.OnClickListener() {


        @Override
        public void onClick(View view) {
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
           //Varmistetaan että löytyy kameraAktiviteetti
            if (intent.resolveActivity(getPackageManager()) != null) {
                //Luodaan tiedosto kuvalle
                File photoFile = null;

                try {
                    photoFile = createImageFile();
                    mCurrentPhotoPath = photoFile.getAbsolutePath();
                    Log.d("Kuvan teko", "Onnistui. mCurrentPhotoPath: " + mCurrentPhotoPath);
                } catch (IOException ex) {
                    Log.d("Kuvan teko", "Epäonnistui");
                    ex.printStackTrace();
                    mCurrentPhotoPath = null;
                }

                if (photoFile != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);

            setPic();
            galleryAddPic();
        }
        if (requestCode == IMAGE_PICKER_SELECT && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = getBitmapFromCameraData(data, getApplicationContext());
            Uri uri = data.getData();
            try {
                mCurrentPhotoPath = getRealPathFromURI(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialoginKuva.setImageBitmap(bitmap);
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    // Tiedoston teko täyden kuvan ottamista varten.

    private File createImageFile() throws  IOException {
        // Luodaan tiedostolle nimi
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, /* Tiedoston nimen alkuosa */
                ".jpg", /* Tiedoston nimen loppuosa */
                storageDir /* Kansio jossa tiedosto sijaitsee */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void setPic() {
        // Skaalataan kuva oikein dialoginkuvaan:

        // Dialoginkuva imageView:n leveys ja pituus
        int targetW = dialoginKuva.getWidth();
        int targetH = dialoginKuva.getHeight();

        //Bitmapin koot
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Kuinka paljon skaalataan kuvaa
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Dekoodataan kuvatiedosto bitmap kokoon ja laitetaan näkymään
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        // Laitetaan tämä kuva preview:hin ja jos painetaan tallenna niin otetaan kokonainen kuva
        // Kännykän kansiosta!
        dialoginKuva.setImageBitmap(bitmap);
    }

    private class JSONSendImageAndInfo extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            AsyncClass jsonClass = new AsyncClass();
            //TODO global variables
            jsonClass.setUsername(app.getUsername());
            jsonClass.setPassword(app.getPassword());
            jsonClass.setfID(app.getfID());

            String itemID = strings[0];
            String descOfImage = strings[1];
            descForNewPic = descOfImage;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //bm = ((BitmapDrawable)dialoginKuva.getDrawable()).getBitmap();

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;

            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int maxWidth = 640;
            int maxHeight = 480;

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

                // Skaalaa kuva pienemmäksi
                int scaleFactor = Math.min(photoW/maxWidth, photoH/maxHeight);
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;


            // Muunnetaan kuva Base64-muotoon
            Bitmap bitmapToSend = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

            bitmapToSend.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            byte byteArray[] = stream.toByteArray();

            String bytearrayToString = new String(byteArray);

            Log.d("byteArraytoString", bytearrayToString);


            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            Log.d("Debutagaan parametrit", "itemID =" + itemID);

            JSONObject json = null;
            try {
                json = jsonClass.letsInsertPRImageAndInfo(itemID, descOfImage, encodedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            if (json != null) {
                ImageAndText item = new ImageAndText();

                otsikkoUusienKuvienListViewlle.setVisibility(View.VISIBLE);
                listViewJossaOnUusiaKuvia.setVisibility(View.VISIBLE);

                item.setImagePath(mCurrentPhotoPath);
                item.setText(descForNewPic);
                listOfImageAndText.add(item);
                adapterForNewPics.notifyDataSetChanged();
            }
        }
    }

    private class JSONUpdatePRImageItem extends AsyncTask<String, JSONObject, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {

            AsyncClass json = new AsyncClass();
            json.setUsername(app.getUsername());
            json.setPassword(app.getPassword());
            json.setfID(app.getfID());

            String IDofPicture = strings[0];
            String newInfoForPicture = strings[1];

            JSONObject obj = null;
            try {
                 obj = json.letsUpdatePRImageItemInfo(IDofPicture, desiredAttributeForUpdate,
                        newInfoForPicture,itemIDforQuery);
            } catch (Exception e) {
                e.printStackTrace();
            }
             return obj;
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            super.onPostExecute(object);
            if (object != null) {
             Log.d("Päivitys taisi onnistua", "Object:" + object.toString());
                // Tässä lisätään uudet tiedot
                Item item = listOfItems.get(listItemToModify);
                item.setItemInfo(newDescForListItem);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void setCredentialsFromPreferences() {
        // avataan yhteys preferensseihin
        mSharedPreferences = this.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        //luetaan käyttäjän nimi ja salasana
        String familiarUser = mSharedPreferences.getString(PREF_NAME, "");
        String familiarPassword = mSharedPreferences.getString(PREF_PASSWORD, "");

        app.setUsername(familiarUser);
        app.setPassword(familiarPassword);
        app.setfID(mSharedPreferences.getString(PREF_FID, ""));
    }

    // Kuvan poistaminen

    private class JSONRemovePRImageItem extends AsyncTask<String, JSONObject, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {

            AsyncClass json = new AsyncClass();
            json.setUsername(app.getUsername());
            json.setPassword(app.getPassword());
            json.setfID(app.getfID());

            String IDofPicture = pictureToDelete;

            JSONObject obj = null;
            try {
                Log.d("Tämä poistaa kuvan", "Kuvan itemID: " + IDofPicture);
                obj = json.letsDeletePRImageItem(IDofPicture, itemIDforQuery);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return obj;
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            super.onPostExecute(object);
            if (object != null) {
                // Tässä poistetaan tämä itemi listasta
                dialogForModification.dismiss();

                listOfItems.remove(listItemToModify);
                adapter.notifyDataSetChanged();
            }
        }
    }


    public static Bitmap getBitmapFromCameraData(Intent data, Context context) {
        Uri selectedImage = data.getData();
        String [] filePahtColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(selectedImage, filePahtColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePahtColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return BitmapFactory.decodeFile(picturePath);
    }

    public Bitmap downloadFile(String stringURL, String username, String password) {

        try {
            DefaultHttpClient client = new DefaultHttpClient();
            client.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
                    new UsernamePasswordCredentials(username, password));
            HttpGet request = new HttpGet(stringURL);
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();
            bmImg = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bmImg;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.camperustiedot, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        return super.onOptionsItemSelected(item);
    }

                // Sertifikaatteihin luottaminen
    public static class NukeSSLCerts {
        protected static final String TAG = "NukeSSLCerts";

        public static void nuke() {
            try {
                TrustManager[] trustAllCerts = new TrustManager[] {
                        new X509TrustManager() {
                            public X509Certificate[] getAcceptedIssuers() {
                                X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                                return myTrustedAnchors;
                            }

                            @Override
                            public void checkClientTrusted(X509Certificate[] certs, String authType) {}

                            @Override
                            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                        }
                };

                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {
                        return true;
                    }
                });
            } catch (Exception e) {
            }
        }
    }

}