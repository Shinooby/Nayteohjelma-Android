package siirtyminenfragmenteilla.app.Viewpagerin_fragmentit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import siirtyminenfragmenteilla.app.AsyncClass;
import siirtyminenfragmenteilla.app.CAMperustiedot;
import siirtyminenfragmenteilla.app.R;
import siirtyminenfragmenteilla.app.apuluokat.MyApp;
import siirtyminenfragmenteilla.app.adapterit.MySingleton;


public class yleis_frag extends Fragment  {

    NetworkImageView maatilanKuva;
    public EditText maatilanNimi;
    EditText maatilanYTunnus;
    EditText yhteyshenkilö;
    EditText osoite;
    EditText postiOsoite;
    EditText postiToimiPaikka;
    EditText puhelinNumero;
    EditText fax;
    EditText sähköposti;
    EditText web_osoite;

    EditText maatilanTuotantosuunta;
    EditText lisätietoaTuotantosuunnasta;
    EditText eläimet;
    EditText pinta_alaTiedot;

    CheckBox asuntoOpiskelijalle;

    Button kameranKutsumisButtoni;

    EditText lisätietoaYleisesti;


    int REQUEST_IMAGE_CAPTURE = 20;

    int IMAGE_PICKER_SELECT = 50;
    MyApp app = new MyApp();
    ImageLoader mImageLoader;

    private String mCurrentPhotoPath;

    private String IDnr1 = "IDnr1";
    private String IDnr2 = "IDnr2";
    private String IDnr3 = "IDnr3";
    private String IDnr4 = "IDnr4";
    private String IDnr5 = "IDnr5";
    private String IDnr6 = "IDnr6";
    private String IDnr7 = "IDnr7";
    private String IDnr8 = "IDnr8";
    private String IDnr9 = "IDnr9";
    private String IDnr10 = "IDnr10";
    private String IDnr11 = "IDnr11";
    private String IDnr12 = "IDnr12";
    private String IDnr12Info = "IDnr12Info";
    private String IDnr13 = "IDnr13";
    private String IDnr14 = "IDnr14";
    private String IDnr15 = "IDnr15";
    private String IDnr16 = "IDnr16";

    private String desiredItemID = "farm";

    CAMperustiedot call = new CAMperustiedot();

    NetworkImageView dialoginKuvaFarmista;

    public static final String ACTION_INTENT = "siirtyminenfragmenteilla.app.action.UI_UPDATE";

    protected BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_INTENT.equals(intent.getAction())) {
                String value = intent.getStringExtra("JSONObject");

                app.setUsername(intent.getStringExtra("username"));
                app.setPassword(intent.getStringExtra("password"));
                JSONObject receivedObject = null;
                try {
                  receivedObject = new JSONObject(value);
                   setContentToViews(receivedObject);
                    Log.d("receivedObjectToString", receivedObject.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.yleis_layout2, container, false);
        Log.d("sivujen luonti", "onCreateView");
        maatilanNimi = (EditText) v.findViewById(R.id.maatilanNimiLayout);
        maatilanYTunnus = (EditText) v.findViewById(R.id.ytunnusLayout);
        yhteyshenkilö = (EditText) v.findViewById(R.id.yhteyshenkiloLayout);
        osoite = (EditText) v.findViewById(R.id.osoiteLayout);
        postiOsoite = (EditText) v.findViewById(R.id.postiosoiteLayout);
        postiToimiPaikka = (EditText) v.findViewById(R.id.postitoimipaikkaLayout);
        puhelinNumero = (EditText) v.findViewById(R.id.puhelinnroLayout);
        fax = (EditText) v.findViewById(R.id.faxLayout);
        sähköposti = (EditText) v.findViewById(R.id.sahkopostiLayout);
        web_osoite = (EditText) v.findViewById(R.id.web_osoiteLayout);

        maatilanTuotantosuunta = (EditText) v.findViewById(R.id.maatilanTuotantosuuntaLayout);
        lisätietoaTuotantosuunnasta = (EditText) v.findViewById(R.id.lisatietoaTuotantosuunnastaLayout);
        eläimet = (EditText) v.findViewById(R.id.elaimetLayout);
        pinta_alaTiedot = (EditText) v.findViewById(R.id.pintaalatiedotLayout);

        asuntoOpiskelijalle = (CheckBox) v.findViewById(R.id.asuntoopiskelijalleCheckbox);

        lisätietoaYleisesti = (EditText) v.findViewById(R.id.lisatietoaYleisestiLayout);

        maatilanKuva = (NetworkImageView) v.findViewById(R.id.kuvanlisays);
        mImageLoader = MySingleton.getInstance(getActivity().getApplicationContext()).getImageLoader();

        kameranKutsumisButtoni = (Button) v.findViewById(R.id.buttonKuvanOttamiselle);

        kameranKutsumisButtoni.setOnClickListener(onClickListener);

        maatilanNimi.setOnClickListener(onClickListener);
        maatilanYTunnus.setOnClickListener(onClickListener);
        yhteyshenkilö.setOnClickListener(onClickListener);
        osoite.setOnClickListener(onClickListener);
        postiOsoite.setOnClickListener(onClickListener);
        postiToimiPaikka.setOnClickListener(onClickListener);
        puhelinNumero.setOnClickListener(onClickListener);
        fax.setOnClickListener(onClickListener);
        sähköposti.setOnClickListener(onClickListener);
        web_osoite.setOnClickListener(onClickListener);

        maatilanTuotantosuunta.setOnClickListener(onClickListener);
        lisätietoaTuotantosuunnasta.setOnClickListener(onClickListener);
        eläimet.setOnClickListener(onClickListener);
        pinta_alaTiedot.setOnClickListener(onClickListener);

        asuntoOpiskelijalle.setOnClickListener(onClickListener);
        lisätietoaYleisesti.setOnClickListener(onClickListener);

    return v;
    }

    public void setContentToViews(JSONObject object) {

        if (object != null) {
            try {

                app.setfID(object.getString(IDnr1));

                maatilanNimi.setText(object.getString(IDnr2));
                maatilanYTunnus.setText(object.getString(IDnr3));
                yhteyshenkilö.setText(object.getString(IDnr11));
                osoite.setText(object.getString(IDnr4));
                postiOsoite.setText(object.getString(IDnr5));
                postiToimiPaikka.setText(object.getString(IDnr6));
                puhelinNumero.setText(object.getString(IDnr7));
                fax.setText(object.getString(IDnr8));
                sähköposti.setText(object.getString(IDnr9));
                web_osoite.setText(object.getString(IDnr10));

                maatilanTuotantosuunta.setText(object.getString(IDnr12));
                lisätietoaTuotantosuunnasta.setText(object.getString(IDnr12Info));
                eläimet.setText(object.getString(IDnr13));
                pinta_alaTiedot.setText(object.getString(IDnr14));

                if (object.getString(IDnr15).equals("True")) {
                    asuntoOpiskelijalle.setChecked(true);
                } else
                    asuntoOpiskelijalle.setChecked(false);

                lisätietoaYleisesti.setText(object.getString(IDnr16));

                CAMperustiedot.NukeSSLCerts.nuke();
                CAMperustiedot call = new CAMperustiedot();

                String pathForFarmImage = app.getJSONURLForImages() + "?ID=" + app.getfID() +
                        "&LargestSize=200&ImageType=" + "farm";
                try {
                    maatilanKuva.setImageUrl(pathForFarmImage, mImageLoader);
                } catch (Exception e) {
                    e.printStackTrace();
                    maatilanKuva.setImageResource(R.drawable.lisaakuva);
                }
            } catch (Exception e) {
               e.printStackTrace();
            }
        }

    }

    // TODO: Rename and change types and number of parameters
    public static yleis_frag newInstance(String text) {
        yleis_frag f = new yleis_frag();
        Bundle b = new Bundle();
        f.setArguments(b);

        return f;
    }

    public yleis_frag() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("sivujen luonti", "onCreate");

        IntentFilter filter = new IntentFilter(ACTION_INTENT);
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(receiver, filter);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId())
            {
                case R.id.buttonKuvanOttamiselle:
                        callDialogForNewFarmImage();
                    break;
                case R.id.dialogButton_kuvaKameralla:
                    Log.d("Kamera-buttoni", "Painoit sitä Yleis-Fragmentissa");
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    //Varmistetaan että löytyy kameraAktiviteetti
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
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

                    break;
                case R.id.dialogButton_kuvaGalleriasta:
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, IMAGE_PICKER_SELECT);
                    break;

                case R.id.maatilanNimiLayout:
                    String currentValueNimi = maatilanNimi.getText().toString();

                      callDialogForInput("Maatilan Nimi",
                            currentValueNimi, maatilanNimi, IDnr2, 50);
                break;
                case R.id.ytunnusLayout:
                    String currentValueYtunnus = maatilanYTunnus.getText().toString();
                    callDialogForInput("Maatilan Y-tunnus",
                            currentValueYtunnus, maatilanYTunnus, IDnr3, 50);
                    break;

                case R.id.yhteyshenkiloLayout:
                    String currentValueYhteyshenkilo = yhteyshenkilö.getText().toString();
                    callDialogForInput("Yhteyshenkilö",
                            currentValueYhteyshenkilo, yhteyshenkilö, IDnr11, 50);
                    break;

                case R.id.osoiteLayout:
                    String currentValueOsoite = osoite.getText().toString();
                        callDialogForInput("Osoite",
                                currentValueOsoite, osoite, IDnr4, 20);
                    break;

                case R.id.postiosoiteLayout:
                    Log.d("Painoit postiosoitetta ", "Et sillai");
                     String currentValuePostiosoite = postiOsoite.getText().toString();
                    callDialogForInput("Postiosoite",
                            currentValuePostiosoite,postiOsoite ,IDnr5 , 10);
                    break;

                case R.id.postitoimipaikkaLayout:
                     String currentValuePostitoimipaikka = postiToimiPaikka.getText().toString();
                    callDialogForInput("Postitoimipaikka",
                            currentValuePostitoimipaikka ,postiToimiPaikka ,IDnr6, 20 );
                    break;

                case R.id.puhelinnroLayout:
                    String currentValuePuhelin = puhelinNumero.getText().toString();
                    callDialogForInput("Puhelinnumero",
                            currentValuePuhelin ,puhelinNumero ,IDnr7, 20 );
                    break;

                case R.id.faxLayout:
                    String currentValueFax = fax.getText().toString();
                    callDialogForInput("Fax",
                            currentValueFax ,fax ,IDnr8, 20 );
                    break;
                case R.id.sahkopostiLayout:
                    String currentValueSähköposti = sähköposti.getText().toString();
                    callDialogForInput("Sähköposti",
                            currentValueSähköposti ,sähköposti ,IDnr9, 20);
                    break;

                    case R.id.web_osoiteLayout:
                    String currentValueWeb = web_osoite.getText().toString();
                    callDialogForInput("Web-osoite",
                            currentValueWeb ,web_osoite ,IDnr10, 20);
                    break;

                case R.id.maatilanTuotantosuuntaLayout:
                    String currentValueTuotantosuunta = maatilanTuotantosuunta.getText().toString();
                    callDialogForInput("Maatilan tuotantosuunta",
                            currentValueTuotantosuunta ,maatilanTuotantosuunta ,IDnr12, 100);
                    break;
                    case R.id.lisatietoaTuotantosuunnastaLayout:
                    String currentValueLisätietoaTuotantosuunnasta = lisätietoaTuotantosuunnasta.getText().toString();
                    callDialogForInput("Lisätietoa tuotantosuunnasta",
                            currentValueLisätietoaTuotantosuunnasta ,lisätietoaTuotantosuunnasta ,IDnr12Info, 1000 );
                    break;

                     case R.id.elaimetLayout:
                    String currentValueEläimet = eläimet.getText().toString();
                    callDialogForInput("Eläimet",
                            currentValueEläimet ,eläimet ,IDnr13, 100 );
                    break;

                     case R.id.pintaalatiedotLayout:
                    String currentValuePinta = pinta_alaTiedot.getText().toString();
                    callDialogForInput("Pinta-ala tiedot",
                            currentValuePinta ,pinta_alaTiedot ,IDnr14, 100);
                    break;

                case R.id.asuntoopiskelijalleCheckbox:
                    String currentCheck = "";
                    if (asuntoOpiskelijalle.isChecked())
                    {
                        currentCheck = "true";
                    }
                    else currentCheck = "false";

                     callDialogForCheckbox("Asunto opiskelijalle?",
                            currentCheck ,asuntoOpiskelijalle ,IDnr15 );

                    break;

                case R.id.lisatietoaYleisestiLayout:
                    String currentValueLisätietoaYleisesti = lisätietoaYleisesti.getText().toString();
                    callDialogForInput("Lisätietoa yleisesti",
                            currentValueLisätietoaYleisesti ,lisätietoaYleisesti ,IDnr16, 100 );
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);

            setPic();
            galleryAddPic();

        }
        if (requestCode == IMAGE_PICKER_SELECT && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = getBitmapFromCameraData(data, getActivity().getApplicationContext());
            Uri uri = data.getData();
            try {
                mCurrentPhotoPath = getRealPathFromURI(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialoginKuvaFarmista.setImageBitmap(bitmap);
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
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

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
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

    private void callDialogForCheckbox(String description, String originalCheck,
                                       final CheckBox asuntoOpiskelijalle, final String attribute) {

        int defaultState;
            if (originalCheck.equals("True")) {
                defaultState = 0;
            }
            else {
            defaultState = 1;
            }
        final CharSequence[] items = {" Kyllä ", " Ei "};

        new AlertDialog.Builder(getActivity())
                .setSingleChoiceItems(items, defaultState, null)
                .setPositiveButton("Tallenna", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        String valueToSend = null;

                        if (selectedPosition == 0) {
                            valueToSend = "True";
                            asuntoOpiskelijalle.setChecked(true);
                        }
                        if (selectedPosition == 1) {
                            valueToSend = "False";
                            asuntoOpiskelijalle.setChecked(false);
                        }
                            new JSONUpdate().execute(attribute, valueToSend);

                    }
                })
                .show();
    }

    private void callDialogForInput(String description, final String originalValue, final EditText viewToChange,
                                      final String attribute, int maxChars) {

            final EditText input = new EditText(getActivity());
            input.setText(originalValue);

        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxChars);
        input.setFilters(FilterArray);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setTitle(description)
                    .setView(input)
                    .setPositiveButton("Tallenna", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    }).setNegativeButton("Peruuta", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do nothing.
                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean wantToCloseDialog = false;
// if (!value.matches("[a-zA-Z0-9.,/-?:]*"))
                String value = input.getText().toString();
                if (!value.equals(originalValue)) {
                    if (!value.matches("[a-zA-ZÄ-Öä-öåÅ0-9/?.,: _-]*")) {
                        Toast.makeText(getActivity(), "Tarkista ettei viestisi sisällä seuraavia \n " +
                                "erikoismerkkejä: \"  \' ;", Toast.LENGTH_LONG).show();
                    } else {
                        //eri arvo ==> Tallenna editTextiin
                        viewToChange.setText(value);
                        wantToCloseDialog = true;
                        //Sen jälkeen kutsu muutosmetodia...
                        // käytetään updateFarmSingleAttribute-rajapintaa
                        new JSONUpdate().execute(attribute, value);
                    }
                }else {
                    wantToCloseDialog = true;
                }

                if (wantToCloseDialog)
                    dialog.dismiss();
            }
        });
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private class JSONUpdate extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            AsyncClass jsonClass = new AsyncClass();
            jsonClass.setUsername(app.getUsername());
            jsonClass.setPassword(app.getPassword());
            jsonClass.setfID(app.getfID());

            String value = strings[1];
            String attribute = strings[0];

            JSONObject json = jsonClass.letsUpdateFarmSingleAttributeTake2(attribute, value);

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);

        }
    }

    private void callDialogForNewFarmImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialgo_modifycameraitem, null);

        dialoginKuvaFarmista = (NetworkImageView) view.findViewById(R.id.networkImageViewSuurempaaKuvaaVarten);
        final EditText kuvaus = (EditText) view.findViewById(R.id.dialogin_kuvauksen_editText);
        final TextView kuvauksenOtsikko = (TextView) view.findViewById(R.id.txt_dialogin_kuvaus);

        Button uusiKuvaKameralla = (Button) view.findViewById(R.id.dialogButton_kuvaKameralla);
        Button uusiKuvaGalleriasta = (Button) view.findViewById(R.id.dialogButton_kuvaGalleriasta);
        Button kuvanPoistaminen = (Button) view.findViewById(R.id.kuvanPoistamisNappi);
        kuvanPoistaminen.setVisibility(view.GONE);
        kuvaus.setVisibility(view.GONE);
        kuvauksenOtsikko.setVisibility(view.GONE);

        uusiKuvaKameralla.setOnClickListener(onClickListener);
        uusiKuvaGalleriasta.setOnClickListener(onClickListener);

        String urlForLargerImage = app.getJSONURLForImages() + "?ID=" + app.getfID()
                + "&LargestSize=300&ImageType=" + desiredItemID;
        dialoginKuvaFarmista.setImageUrl(urlForLargerImage, mImageLoader);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Tallenna", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("Saadaanko tekstit?", "TEST");

                                new JSONUpdateFarmImage().execute();
                    }
                })
                .setNegativeButton("Peruuta", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Älä tee mitään
                    }
                });
        builder.create().show();
    }

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
        int targetW = dialoginKuvaFarmista.getWidth();
        int targetH = dialoginKuvaFarmista.getHeight();

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
        // Puhelimen kansiosta
        dialoginKuvaFarmista.setImageBitmap(bitmap);
    }

    private class JSONUpdateFarmImage extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            AsyncClass jsonClass = new AsyncClass();
            jsonClass.setUsername(app.getUsername());
            jsonClass.setPassword(app.getPassword());
            jsonClass.setfID(app.getfID());

            // TODO kuvan hakeminen mCurrentPhotoPathista, koon muuttaminen, compressointi ja base64:ksi muuttaminen
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
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

            Bitmap bitmapToSend = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            bitmapToSend.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            byte byteArray[] = stream.toByteArray();

            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

            Log.d("Debutagaan parametrit", "itemID =" + desiredItemID);

            JSONObject json = null;
            try {
                    json = jsonClass.letsUpdateFarmImage(encodedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return json;

        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);

            if (json != null) {
                Log.d("Saatiin viesti", "Kutsu networkImageView:n urlia uudestaan");

                File imgFile = new File(mCurrentPhotoPath);
                    if (imgFile.exists()) {

                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                        bmOptions.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                        int maxWidth = maatilanKuva.getWidth();
                        int maxHeight = maatilanKuva.getHeight();
                        int photoW = bmOptions.outWidth;
                        int photoH = bmOptions.outHeight;

                        // Skaalaa kuva pienemmäksi
                        int scaleFactor = Math.min(photoW/maxWidth, photoH/maxHeight);
                        bmOptions.inJustDecodeBounds = false;
                        bmOptions.inSampleSize = scaleFactor;

                        Bitmap bitmapToSend = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

                        maatilanKuva.setImageBitmap(bitmapToSend);
                    }
                else {
                        Log.d("Tiedostoa ei löytynyt", "mCurrentphotopath: " + mCurrentPhotoPath);
                    }

            }
        }
    }
}
