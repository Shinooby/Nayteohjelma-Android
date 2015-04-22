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
import siirtyminenfragmenteilla.app.R;
import siirtyminenfragmenteilla.app.apuluokat.ApprovalStates;
import siirtyminenfragmenteilla.app.apuluokat.MyApp;
import siirtyminenfragmenteilla.app.yritystoiminnan_alisivut.YllapitoJaKehittaminen;
import siirtyminenfragmenteilla.app.yritystoiminnan_alisivut.tulevaisuudenNakymat;
import siirtyminenfragmenteilla.app.yritystoiminnan_alisivut.yrityksenMenestystekijat;


public class yritys_frag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private ImageButton tulevaisuudenNäkymät;
    private ImageButton ylläpitoJaKehittäminen;
    private ImageButton yrityksenMenestystekijät;

    private TextView arviointiOtsikko;
    private TextView arvioinninLisätietoa;

    private String wantedSection = "yritystoiminta";

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
        View v = inflater.inflate(R.layout.yritystoiminta, container, false);

        tulevaisuudenNäkymät = (ImageButton)v.findViewById(R.id.tulevaisuudennakyma);
        ylläpitoJaKehittäminen = (ImageButton)v.findViewById(R.id.yllapitoJaKehittaminenButton);
        yrityksenMenestystekijät = (ImageButton)v.findViewById(R.id.yrityksenMenestystekijatButton);

        tulevaisuudenNäkymät.setOnClickListener(onClickListener);
        ylläpitoJaKehittäminen.setOnClickListener(onClickListener);
        yrityksenMenestystekijät.setOnClickListener(onClickListener);

        arviointiOtsikko = (TextView) v.findViewById(R.id.yritystoiminnanArviointiOtsikko);
        arvioinninLisätietoa = (TextView) v.findViewById(R.id.yritystoiminnanArviointiLisätietoa);

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
        setCredentialsFromPreferences();
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
            switch (v.getId()) {
                case R.id.tulevaisuudennakyma:
                    Log.d("Yritystoiminta", "Tulevaisuuden näkymät Button");
                    Intent intentTulevaisuudenNakyma = new Intent(getActivity(),tulevaisuudenNakymat.class);
                    startActivity(intentTulevaisuudenNakyma);
                    break;
                case R.id.yllapitoJaKehittaminenButton:
                    Log.d("Yritystoiminta", "Ylläpito ja kehittäminen Button");
                    Intent intentYllapitoJaKehitys = new Intent(getActivity(),YllapitoJaKehittaminen.class);
                    startActivity(intentYllapitoJaKehitys);
                    break;
                case R.id.yrityksenMenestystekijatButton:
                    Log.d("Yritystoiminta", "Yrityksen menestystekijät button");
                    Intent intentMenestystekijät = new Intent(getActivity(),yrityksenMenestystekijat.class);
                    startActivity(intentMenestystekijät);
                    break;
            }
        }
    };

    public static yritys_frag newInstance(String param1) {
        yritys_frag fragment = new yritys_frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }
    public yritys_frag() {
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
                Log.d("Tapahtui virhe", e.toString());
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
                // JSONObject objectToInspect =  null;
                //jarray.getJSONObject(i);
                //  String objectSection = objectToInspect.getString("saSection");

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

}
