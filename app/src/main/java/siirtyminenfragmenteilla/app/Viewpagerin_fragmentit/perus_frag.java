package siirtyminenfragmenteilla.app.Viewpagerin_fragmentit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;

import siirtyminenfragmenteilla.app.AsyncClass;
import siirtyminenfragmenteilla.app.CAMperustiedot;
import siirtyminenfragmenteilla.app.R;
import siirtyminenfragmenteilla.app.apuluokat.ApprovalStates;
import siirtyminenfragmenteilla.app.apuluokat.MyApp;
import siirtyminenfragmenteilla.app.perusfragmentin_alisivut.maatilanLisatiedot;
import siirtyminenfragmenteilla.app.perusfragmentin_alisivut.tyoHarjoittelunlisatiedot;


public class perus_frag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageButton tyoharjoittelunLisatiedot;
    private ImageButton lisatietoaMaatilasta;
    private ImageButton koneet;
    private ImageButton tuotantoRakennukset;
    private ImageButton tuotantorakennustenTekniikka;
    private ImageButton viljeltavatKasvit;
    private ImageButton rakennusProjektit;
    private ImageButton työTurvallisuus;

    public TextView arviointiOtsikko;
    public TextView arvioinninLisätietoa;

    private static final String PREFS = "prefs";
    //talletettavat tiedot
    public static final String PREF_NAME = "name";
    public static final String PREF_PASSWORD = "password";
    public static final String PREF_FID = "fid";
    public SharedPreferences mSharedPreferences;

   private  MyApp app = new MyApp();

    private String itemID;

    private String wantedSection = "perustiedot";

    private JSONArray arrayToReceive;

    private ApprovalStates[] arrayOfApprovals;

    public perus_frag() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setCredentialsFromPreferences();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.perustiedot, container, false);

        tyoharjoittelunLisatiedot = (ImageButton) v.findViewById(R.id.tyoharjoittelunLisatiedotButton);
        lisatietoaMaatilasta = (ImageButton) v.findViewById(R.id.lisatietoaMaatilastaButton);
        koneet = (ImageButton) v.findViewById(R.id.koneetButton);
        tuotantoRakennukset = (ImageButton) v.findViewById(R.id.tuotantorakennuksetButton);
        tuotantorakennustenTekniikka = (ImageButton) v.findViewById(R.id.tuotantorakennustenTekniikkaButton);
        viljeltavatKasvit = (ImageButton) v.findViewById(R.id.viljeltavatKasvitButton);
        rakennusProjektit = (ImageButton) v.findViewById(R.id.rakennusprojektitButton);
        työTurvallisuus = (ImageButton) v.findViewById(R.id.tyoTurvallisuusButton);

        tyoharjoittelunLisatiedot.setOnClickListener(onClickListener);
        lisatietoaMaatilasta.setOnClickListener(onClickListener);
        koneet.setOnClickListener(onClickListener);
        tuotantoRakennukset.setOnClickListener(onClickListener);
        tuotantorakennustenTekniikka.setOnClickListener(onClickListener);
        viljeltavatKasvit.setOnClickListener(onClickListener);
        rakennusProjektit.setOnClickListener(onClickListener);
        työTurvallisuus.setOnClickListener(onClickListener);

        getChildFragmentManager();

        arviointiOtsikko = (TextView) v.findViewById(R.id.perustiedotArviointiEdit);
        arvioinninLisätietoa = (TextView) v.findViewById(R.id.perustiedotArviointiTarkennus);

        if (arrayOfApprovals == null) {
            Log.d("arrayofApprovalsin koko", "On null --> Tehdään uusi haku ");
            new JSONApproval().execute();
        }  else {
            Log.d("arrayofApprovalsin koko", "Ei ole null --> Asetetaan tiedot Arrayn perusteella ");
            updateApproval(arrayOfApprovals);
        }

        return v;
    }

    private void setCredentialsFromPreferences() {
        // avataan yhteys preferensseihin
        mSharedPreferences = this.getActivity().getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        //luetaan käyttäjän nimi ja salasana
        String familiarUser = mSharedPreferences.getString(PREF_NAME, "");
        String familiarPassword = mSharedPreferences.getString(PREF_PASSWORD, "");
        String fIDfromPref = mSharedPreferences.getString(PREF_FID, "");

        app.setUsername(familiarUser);
        app.setPassword(familiarPassword);
        app.setfID(fIDfromPref);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            JSONGetPRImageInfo task;

            switch (v.getId()) {
                case R.id.tyoharjoittelunLisatiedotButton:
                    Log.d("Perustiedot", "Tyoharjoittelunlisatiedot Button");
                    Intent intent = new Intent(getActivity(), tyoHarjoittelunlisatiedot.class);
                    startActivity(intent);
                    break;
                case R.id.lisatietoaMaatilastaButton:
                    Log.d("Perustiedot", "lisatietoa maatilasta Button");

                    Intent intent1 = new Intent(getActivity(),maatilanLisatiedot.class);
                    startActivity(intent1);
                    break;
                case R.id.koneetButton:
                    Log.d("Perustiedot", "koneet Button");
                     itemID = "currentID";

                    task = new JSONGetPRImageInfo();
                    task.execute(itemID);
                        break;
                case R.id.tuotantorakennuksetButton:
                    Log.d("Perustiedot", "tuotantorakennukset Button");
                    itemID = "currentID";

                    task = new JSONGetPRImageInfo();
                    task.execute(itemID);
                    break;
                case R.id.tuotantorakennustenTekniikkaButton:
                    Log.d("Perustiedot", "tuotantorakennusten tekniikka Button");
                    itemID = "currentID";
                    task = new JSONGetPRImageInfo();
                    task.execute(itemID);
                    break;
                case R.id.viljeltavatKasvitButton:
                    Log.d("Perustiedot", "viljeltavat Button");
                    itemID = "currentID";
                    task = new JSONGetPRImageInfo();
                    task.execute(itemID);
                    break;
                case R.id.rakennusprojektitButton:
                    Log.d("Perustiedot", "rakennusProjekti Button");
                    itemID = "currentID";
                    task = new JSONGetPRImageInfo();
                    task.execute(itemID);
                    break;
                case R.id.tyoTurvallisuusButton:
                    Log.d("Perustiedot", "Työturvallisuus Button");
                    itemID = "currentID";
                    task = new JSONGetPRImageInfo();
                    task.execute(itemID);
                    break;
            }
        }
    };

    private void updateApproval(ApprovalStates[] jarray) {

        Log.d("Kutsu vastaanotettu", "");

        if (jarray != null) {
            for (int i = 0; i < jarray.length; i++) {

                if (jarray[i].getSaSection().equals(wantedSection)) {
                    String status = jarray[i].getSaStatus();
                    Log.d("Statuksen debuggaus", "status = " + status);

                    if (status.equals("complete")) {
                        arviointiOtsikko.setText("Täydennettävää");
                        arviointiOtsikko.setTextColor(getResources().getColor(R.color.red));
                        arvioinninLisätietoa.setText(jarray[i].getSaInfo());
                        break;
                    }
                    if (status.equals("accepted")) {
                        arviointiOtsikko.setText("Hyväksytty");
                        arviointiOtsikko.setTextColor(getResources().getColor(R.color.green));
                        arvioinninLisätietoa.setText(jarray[i].getSaInfo());
                        break;
                    }
                    if (status.equals("nothandled")) {
                        arviointiOtsikko.setText("Ei käsitelty");
                        arviointiOtsikko.setTextColor(getResources().getColor(R.color.Gray));
                        arvioinninLisätietoa.setText(jarray[i].getSaInfo());
                        break;
                    }
                }

            }

        }
    }

    public static perus_frag newInstance(String param1) {
        perus_frag fragment = new perus_frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);

        return fragment;
    }


    private class JSONApproval extends AsyncTask<String, String, ApprovalStates[]> {

        @Override
        protected ApprovalStates[] doInBackground(String... strings) {
            AsyncClass jsonClass = new AsyncClass();
            jsonClass.setUsername(app.getUsername());
            jsonClass.setPassword(app.getPassword());
            jsonClass.setfID(app.getfID());

            ObjectMapper mapper = new ObjectMapper();

            ApprovalStates[] approval = null;

            JSONArray jarray = null;
            try {
                jarray = jsonClass.letsGetEvaluation();
                approval = mapper.readValue(jarray.toString().getBytes(), ApprovalStates[].class);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return approval;
        }

        @Override
        protected void onPostExecute(ApprovalStates[] appr) {
            super.onPostExecute(appr);
            arrayOfApprovals = appr;
                  updateApproval(appr);
        }
    }

    private class JSONGetPRImageInfo extends AsyncTask<String, String, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... strings) {
            AsyncClass jsonClass = new AsyncClass();
            String itemID = strings[0];
            JSONArray json = null;
            jsonClass.setUsername(app.getUsername());
            jsonClass.setPassword(app.getPassword());

            jsonClass.setfID(app.getfID());
            try {
                json = jsonClass.letsGetPRImageItemInfo(itemID);
                } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
            // return null;
        }

        @Override
        protected void onPostExecute(JSONArray json) {
            super.onPostExecute(json);
            Log.d("Tietojen haun asynctask", "onPostExecute");
            if (json != null) {
                arrayToReceive = json;
                Intent intentForCameraActivities = new Intent(getActivity().getApplicationContext(), CAMperustiedot.class);
                intentForCameraActivities.putExtra("ID_TO_GET", itemID);
                intentForCameraActivities.putExtra("jsonarray", arrayToReceive.toString());
                startActivity(intentForCameraActivities);
            }
             else {
                Intent intentForCameraActivities = new Intent(getActivity().getApplicationContext(), CAMperustiedot.class);
                startActivity(intentForCameraActivities);
            }
        }
        }
    }

