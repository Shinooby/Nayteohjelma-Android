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
import siirtyminenfragmenteilla.app.kotielaintuotannon_alisivut.ElaintenRuokintaAlisivu;
import siirtyminenfragmenteilla.app.kotielaintuotannon_alisivut.kotielainJalostus;
import siirtyminenfragmenteilla.app.kotielaintuotannon_alisivut.lypsyRutiinitAlisivu;
import siirtyminenfragmenteilla.app.kotielaintuotannon_alisivut.terveydenHuoltoAlisivu;
import siirtyminenfragmenteilla.app.kotielaintuotannon_alisivut.tuotosTasotAlisivu;
import siirtyminenfragmenteilla.app.kotielaintuotannon_alisivut.tuotteetAlisivu;


public class kotielain_frag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private ImageButton tuotantoKierto;
    private ImageButton lypsyRutiinit;
    private ImageButton elaintenRuokinta;
    private ImageButton kirjanPito;
    private ImageButton tuotosTasot;
    private ImageButton tuotteet;
    private ImageButton kotieläinJalostus;
    private ImageButton terveydenHuolto;
    private ImageButton erityisetTapahtumat;

    private TextView arviointiOtsikko;
    private TextView arvioinninLisätietoa;

    private String wantedSection = "elaintuotanto";

    private JSONArray arrayToReceive;

    String itemID;

    MyApp app = new MyApp();

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
        View v = inflater.inflate(R.layout.kotielaintuotanto, container, false);

        setCredentialsFromPreferences();

        tuotantoKierto = (ImageButton) v.findViewById(R.id.tuotantoKiertoButton);
        lypsyRutiinit = (ImageButton) v.findViewById(R.id.lypsyrutiinitButton);
        elaintenRuokinta = (ImageButton) v.findViewById(R.id.elaintenRuokintaButton);
        kirjanPito = (ImageButton) v.findViewById(R.id.kirjanpitoButton);
        tuotosTasot = (ImageButton) v.findViewById(R.id.tuotostasotButton);
        tuotteet = (ImageButton) v.findViewById(R.id.tuotteetButton);
        kotieläinJalostus = (ImageButton) v.findViewById(R.id.kotielainJalostusButton);
        terveydenHuolto = (ImageButton) v.findViewById(R.id.terveydenhuoltoButton);
        erityisetTapahtumat = (ImageButton) v.findViewById(R.id.erityisetTapahtumatButton);

        tuotantoKierto.setOnClickListener(onClickListener);
        lypsyRutiinit.setOnClickListener(onClickListener);
        elaintenRuokinta.setOnClickListener(onClickListener);
        kirjanPito.setOnClickListener(onClickListener);
        tuotosTasot.setOnClickListener(onClickListener);
        tuotteet.setOnClickListener(onClickListener);
        kotieläinJalostus.setOnClickListener(onClickListener);
        terveydenHuolto.setOnClickListener(onClickListener);
        erityisetTapahtumat.setOnClickListener(onClickListener);

         arviointiOtsikko = (TextView) v.findViewById(R.id.kotieläintuotannonArvointiOtsikko);
         arvioinninLisätietoa = (TextView) v.findViewById(R.id.kotieläintuotannonArvointiLisätietoa);


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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tuotantoKiertoButton:
                    Log.d("Kotieläintuotanto", "Tuotantokierto Button");
                    Intent intentTuotantokierto = new Intent(getActivity(), siirtyminenfragmenteilla.app.kotielaintuotannon_alisivut.tuotantoKierto.class);
                    startActivity(intentTuotantokierto);
                    break;
                case R.id.lypsyrutiinitButton:
                    Log.d("Kotieläintuotanto", "lypsyrutiinit Button");
                    Intent intentLypsyrutiinit = new Intent(getActivity(), lypsyRutiinitAlisivu.class);
                    startActivity(intentLypsyrutiinit);
                    break;
                case R.id.elaintenRuokintaButton:
                    Log.d("Kotieläintuotanto", "Elaintenruokinta Button");
                    Intent intentElaintenRuokinta = new Intent(getActivity(), ElaintenRuokintaAlisivu.class);
                    startActivity(intentElaintenRuokinta);
                    break;
                case R.id.kirjanpitoButton:
                    Log.d("Kotieläintuotanto", "kirjanpito Button");
                    Intent intentKirjanpito = new Intent(getActivity(), siirtyminenfragmenteilla.app.kotielaintuotannon_alisivut.kirjanPito.class);
                    startActivity(intentKirjanpito);
                    break;
                case R.id.tuotostasotButton:
                    Log.d("Kotieläintuotanto", "Tuotostasot Button");
                    Intent intentTuotostasotAlisivu = new Intent(getActivity(), tuotosTasotAlisivu.class);
                    startActivity(intentTuotostasotAlisivu);
                    break;
                case R.id.tuotteetButton:
                    Log.d("Kotieläintuotanto", "Tuotteet Button");
                    Intent intentTuotteetAlisivu = new Intent(getActivity(), tuotteetAlisivu.class);
                    startActivity(intentTuotteetAlisivu);
                    break;
                case R.id.kotielainJalostusButton:
                    Log.d("Kotieläintuotanto", "Kotieläinjalostus Button");
                    Intent intentKotielain = new Intent(getActivity(), kotielainJalostus.class);
                    startActivity(intentKotielain);
                    break;
                case R.id.terveydenhuoltoButton:
                    Log.d("Kotieläintuotanto", "Terveydenhuolto Button");
                    Intent intentTerveydenhuolto = new Intent(getActivity(), terveydenHuoltoAlisivu.class);
                    startActivity(intentTerveydenhuolto);
                    break;
                case R.id.erityisetTapahtumatButton:
                    Log.d("Kotieläintuotanto", "Erityiset tapahtumat Button");
                    itemID = "animalspecialevent";
                    new JSONGetPRImageInfo().execute(itemID);

                    break;
            }
        }
    };

    public static kotielain_frag newInstance(String param1) {
        kotielain_frag fragment = new kotielain_frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }
    public kotielain_frag() {
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
