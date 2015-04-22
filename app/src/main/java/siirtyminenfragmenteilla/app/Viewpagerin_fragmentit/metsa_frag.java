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
import siirtyminenfragmenteilla.app.metsatalouden_alisivut.metsikonPerustiedot;
import siirtyminenfragmenteilla.app.metsatalouden_alisivut.metsikonTalous;


public class metsa_frag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";


    private String mParam1;

    private ImageButton metsikönPerustiedot;
    private ImageButton taimikonHoito;
    private ImageButton kasvatusHakkuut;
    private ImageButton metsänUudistaminen;
    private ImageButton metsänTerveys;
    private ImageButton metsätaloudenKoneetjaKalusto;
    private ImageButton metsikönTalous;
    private ImageButton tehdytMetsänhoitotyöt;

    private TextView arviointiOtsikko;
    private TextView arvioinninLisätietoa;

    private String wantedSection = "metsatalous";

    MyApp app = new MyApp();

    private JSONArray arrayToReceive;

    private String itemID;

    private static final String PREFS = "prefs";
    //talletettavat tiedot
    public static final String PREF_NAME = "name";
    public static final String PREF_PASSWORD = "password";
    public static final String PREF_FID = "fid";
    public SharedPreferences mSharedPreferences;

    private ApprovalStates[] arrayOfApprovals;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.metsatalous, container, false);

        setCredentialsFromPreferences();

        metsikönPerustiedot = (ImageButton)v.findViewById(R.id.metsikonPerustiedotButton);
        taimikonHoito = (ImageButton)v.findViewById(R.id.taimikonHoitoButton);
        kasvatusHakkuut = (ImageButton)v.findViewById(R.id.kasvatusHakkuutButton);
        metsänUudistaminen = (ImageButton)v.findViewById(R.id.metsanUudistaminenButton);
        metsänTerveys = (ImageButton)v.findViewById(R.id.metsanTerveysButton);
        metsätaloudenKoneetjaKalusto = (ImageButton)v.findViewById(R.id.metsataloudenKoneetjaKalustoButton);
        metsikönTalous = (ImageButton)v.findViewById(R.id.metsikonTalousButton);
        tehdytMetsänhoitotyöt = (ImageButton)v.findViewById(R.id.tehdytMetsanhoitotyotButton);

        metsikönPerustiedot.setOnClickListener(onClickListener);
        taimikonHoito.setOnClickListener(onClickListener);
        kasvatusHakkuut.setOnClickListener(onClickListener);
        metsänUudistaminen.setOnClickListener(onClickListener);
        metsänTerveys.setOnClickListener(onClickListener);
        metsätaloudenKoneetjaKalusto.setOnClickListener(onClickListener);
        metsikönTalous.setOnClickListener(onClickListener);
        tehdytMetsänhoitotyöt.setOnClickListener(onClickListener);

         arviointiOtsikko = (TextView) v.findViewById(R.id.metsätaloudenArviointiOtsikko);
         arvioinninLisätietoa = (TextView)v.findViewById(R.id.metsätaloudenArviointiLisätietoa);

        if (arrayOfApprovals == null) {
            Log.d("arrayofApprovalsin koko", "On null --> Tehdään uusi haku ");
            new JSONApproval().execute();
        }  else {
            Log.d("arrayofApprovalsin koko", "Ei ole null --> Asetetaan tiedot Arrayn perusteella ");
            updateApproval(arrayOfApprovals);
        }
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

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
            JSONGetPRImageInfo task = new JSONGetPRImageInfo();
            switch (v.getId()) {
                case R.id.metsikonPerustiedotButton:
                    Log.d("Metsätalous", "metsikön perustiedot button");
                    Intent intentMetsikonPerustiedot = new Intent(getActivity(),metsikonPerustiedot.class);
                    startActivity(intentMetsikonPerustiedot);
                    break;
                case R.id.taimikonHoitoButton:
                    Log.d("Metsätalous", "Taimikon hoito button");
                    itemID = "forestseedling";
                    task.execute(itemID);
                    break;
                case R.id.kasvatusHakkuutButton:
                    Log.d("Metsätalous", "Kasvatushakkuut Button");
                    itemID = "forestlogging";
                    task.execute(itemID);
                    break;
                case R.id.metsanUudistaminenButton:
                    Log.d("Metsätalous", "Metsän uudistaminen Button");
                    itemID = "forestrenewal";
                    task.execute(itemID);
                    break;
                case R.id.metsanTerveysButton:
                    Log.d("Metsätalous", "Metsän terveys Button");
                    itemID = "foresthealth";
                    task.execute(itemID);
                    break;
                case R.id.metsataloudenKoneetjaKalustoButton:
                    Log.d("Metsätalous", "Metsätalouden koneet ja kalusto Button");
                    itemID = "forestmachines";
                    task.execute(itemID);
                    break;
                case R.id.metsikonTalousButton:
                    Log.d("Metsätalous", "Metsätalouden koneet ja kalusto Button");
                    Intent intentMetsikonTalous = new Intent(getActivity(),metsikonTalous.class);
                    startActivity(intentMetsikonTalous);
                    break;
                case R.id.tehdytMetsanhoitotyotButton:
                    Log.d("Metsätalous", "Tehdyt metsänhoitotyöt Button");
                    itemID = "forestworks";
                    task.execute(itemID);
                    break;
            }
        }
    };

    public static metsa_frag newInstance(String param1) {
        metsa_frag fragment = new metsa_frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }
    public metsa_frag() {
        // Required empty public constructor
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
                String test = approval[2].getSaSection();

                Log.d("Testaus", "SaSection = " + test);
            } catch (Exception e) {
            }
            return approval;
        }

        @Override
        protected void onPostExecute(ApprovalStates[] json) {
            super.onPostExecute(json);
            arrayOfApprovals = json;
            updateApproval(json);
        }
    }

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
                Log.d("For-lauseen tarkkailu", "i = " + i);
            }

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
