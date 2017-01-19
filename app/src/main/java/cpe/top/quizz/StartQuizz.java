package cpe.top.quizz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import cpe.top.quizz.beans.Question;
import cpe.top.quizz.beans.Quizz;
import cpe.top.quizz.beans.Response;
import cpe.top.quizz.beans.Theme;
import cpe.top.quizz.beans.User;
import cpe.top.quizz.utils.JustifiedTextView;

/**
 * @author Maxence Royer
 * @version 0.1
 * @since 06/11/2016
 */
public class StartQuizz extends AppCompatActivity {

    private static final String QUIZZ = "QUIZZ";
    private static final String USER = "USER";
    private static final String TIMER = "TIMER";
    private static final String EVALUATIONID = "EVALUATIONID";
    private static final String GOODQUESTIONS = "GOODQUESTIONS";
    private static final String BADQUESTIONS = "BADQUESTIONS";
    private static final Long TIMER_REFRESH = 1000L;
    private List<Question> questionsOk = new ArrayList<Question>();
    private JustifiedTextView jusTextView;
    private TextView nameQuestion;
    private List<Response> listResponses;
    private List<Button> listButtonsView;
    private int numQuestion = 1;
    private int goodQuestions = 0, badQuestions = 0;
    private boolean isClickable = true;
    private int nbClicksButton = 0;
    private Drawable bgButton;
    private Quizz quizz;
    private User connectedUser = null;
    private int timerInt;
    private int evaluationId;
    private TextView timerQuizzValue;
    private Handler handler;
    private Question questionInProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start_quizz);
        Intent intent = getIntent();
        if (intent != null && intent.getSerializableExtra(USER) != null && intent.getSerializableExtra(QUIZZ) != null) {
            timerInt = (Integer) getIntent().getSerializableExtra(TIMER);
            evaluationId = (Integer) getIntent().getSerializableExtra(EVALUATIONID);
            connectedUser = (User) getIntent().getSerializableExtra(USER);
            quizz = (Quizz) getIntent().getSerializableExtra(QUIZZ);
            connectedUser = (User) getIntent().getSerializableExtra(USER);

            if (quizz.getName() != null) {
                Toast.makeText(StartQuizz.this, "Quizz " + quizz.getName() + " récupéré", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(StartQuizz.this, "Oops... Mauvais format de quiz récupéré...", Toast.LENGTH_SHORT).show();
            }
        }

        if(timerInt != -1){
            handler = new Handler();
            handler.postDelayed(runnable, TIMER_REFRESH);
        }


        // Initialisation of the first question & responses associated
        if (quizz != null) {
            // Collection of questions & iterator associated
            Collection<Question> questions = quizz.getQuestions();
            List<Question> questionList = new ArrayList<Question>(questions);
            Collections.shuffle(questionList);
            Iterator<Question> questionsItr = questionList.iterator();
            listButtonsView = new ArrayList<Button>();

            // The first question
            Question firstQuestion = questionsItr.next();
            questionInProgress = firstQuestion;
            questionsOk.add(firstQuestion);

            this.setNameQuestionQuizz(firstQuestion.getLabel());

            // Buttons - proposals responses of the quizz
            Collection<Response> responses = firstQuestion.getReponses();
            Iterator<Response> responsesItr = responses.iterator();

            this.setProposalsResponsesQuizz(responsesItr, firstQuestion);

            // Themes of the question
            Collection<Theme> themes = firstQuestion.getThemes();
            Iterator<Theme> themesItr = themes.iterator();

            this.setThemesQuestion(themesItr);

            if (timerInt > 0) {
                RelativeLayout timerQuizz = (RelativeLayout) findViewById(R.id.timerQuizz);
                timerQuizz.setVisibility(View.VISIBLE);

                timerQuizzValue = (TextView) findViewById(R.id.timerQuizzValue);
                timerQuizzValue.setText(String.valueOf(timerInt));
            } else {
                RelativeLayout timerQuizz = (RelativeLayout) findViewById(R.id.timerQuizz);
                timerQuizz.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * private function used to set the themes of the question (IHM)
     *
     * @param themesItr iterator of objects Theme
     */
    private void setThemesQuestion(Iterator<Theme> themesItr) {
        TextView themesQuizz = (TextView) findViewById(R.id.themes);
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("Q" + numQuestion + ". ");
        while (themesItr.hasNext()) {
            Theme current = themesItr.next();
            strBuilder.append(current.getName());
            if (themesItr.hasNext()) {
                strBuilder.append(" & ");
            }
        }
        themesQuizz.setText(strBuilder);
    }

    /**
     * Private function used to set the title of the question (IHM)
     *
     * @param nameQues the name of the question
     */
    private void setNameQuestionQuizz(String nameQues) {
        TextView nameQuestion = (TextView) findViewById(R.id.nameQuestion);
        nameQuestion.setText(nameQues);
    }

    /**
     * Private function used to set the proposals responses of a question (IHM)
     *
     * @param responsesItr iterator of objects Response
     */
    private void setProposalsResponsesQuizz(Iterator<Response> responsesItr, Question question) {
        Response response = new Response();

        listResponses = new ArrayList<Response>();

        if (responsesItr.hasNext()) {
            Button response1 = (Button) findViewById(R.id.Response1);
            // Save old style background of the button
            this.bgButton = response1.getBackground();
            response = responsesItr.next();
            listResponses.add(response);
            response1.setText(response.getLabel());
            listButtonsView.add(response1);
            this.setOnClick(response1, question, response);
        }

        if (responsesItr.hasNext()) {
            Button response2 = (Button) findViewById(R.id.Response2);
            response = responsesItr.next();
            listResponses.add(response);
            response2.setText(response.getLabel());
            listButtonsView.add(response2);
            this.setOnClick(response2, question, response);
        }

        if (responsesItr.hasNext()) {
            Button response3 = (Button) findViewById(R.id.Response3);
            response = responsesItr.next();
            listResponses.add(response);
            response3.setText(response.getLabel());
            listButtonsView.add(response3);
            this.setOnClick(response3, question, response);
        }

        if (responsesItr.hasNext()) {
            Button response4 = (Button) findViewById(R.id.Response4);
            response = responsesItr.next();
            listResponses.add(response);
            response4.setText(response.getLabel());
            listButtonsView.add(response4);
            this.setOnClick(response4, question, response);
        }
    }

    /**
     * ¨Private function used to check if it's the good response
     *
     * @param btn      button
     * @param response Response associated with the button
     */
    private void setOnClick(final Button btn, final Question question, final Response response) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (isClickable) {
                        if(timerInt != -1){
                            handler.removeCallbacks(runnable);
                        }

                        this.testIfGoodResponse(response);
                    }

                    if (!isClickable) {
                        nbClicksButton++;
                    }

                    // Question already verified <=> click on button <=> change question
                    if (!isClickable && nbClicksButton >= 2) {
                        System.out.println("netxt question");
                        final ScrollView linearTop = (ScrollView) findViewById(R.id.linearTop);

                        // IHM
                        linearTop.removeView(jusTextView);
                        linearTop.addView(nameQuestion);

                        nextQuestionIfExists();
                    }
            }

            /**
             * Private function used to change the button color of a response
             * @param response Response associated with the the button
             */
            private void testIfGoodResponse(Response response) {
                isClickable = false;
                if (response.getValide()) {
                    btn.setBackground(getResources().getDrawable(R.drawable.border_success));
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
                    params.leftMargin = 10;
                    params.rightMargin = 10;
                    params.topMargin = 10;
                    params.bottomMargin = 10;
                    btn.setLayoutParams(params);
                    goodQuestions++;
                } else {
                    btn.setBackground(getResources().getDrawable(R.drawable.border_error));
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
                    params.leftMargin = 10;
                    params.rightMargin = 10;
                    params.topMargin = 10;
                    params.bottomMargin = 10;
                    btn.setLayoutParams(params);
                    this.checkGoodResponseIfError();
                    badQuestions++;
                }
                btn.setTextColor(Color.WHITE);

                // Explanation of response
                final ScrollView linearTop = (ScrollView) findViewById(R.id.linearTop);
                nameQuestion = (TextView) findViewById(R.id.nameQuestion);
                linearTop.removeView(nameQuestion);
                jusTextView = new JustifiedTextView(getApplicationContext(), question.getExplanation());
                jusTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
                linearTop.addView(jusTextView);

                // If click on the interface && isClickable == false <=> next question
                this.addListenersScreenToChangeQuestionOnTouch(findViewById(R.id.screen), linearTop);
                this.addListenersScreenToChangeQuestionOnTouch(findViewById(R.id.divQuestion), linearTop);
                this.addListenersScreenToChangeQuestionOnTouch(findViewById(R.id.divResponses), linearTop);
                this.addListenersScreenToChangeQuestionOnTouch(findViewById(R.id.themes), linearTop);
                this.addListenersScreenToChangeQuestionOnTouch(nameQuestion, linearTop);
            }

            private void addListenersScreenToChangeQuestionOnTouch(View layout, final ScrollView linearTop) {
                layout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View arg0, MotionEvent arg1) {
                        if (isClickable == false) {
                            // IHM
                            linearTop.removeView(jusTextView);
                            linearTop.addView(nameQuestion);

                            nextQuestionIfExists();
                        }
                        return true;
                    }
                });
            }

            /**
             * Private function used to change of question
             */
            private void nextQuestionIfExists() {
                Collection<Question> collectionQuestions = quizz.getQuestions();
                List<Question> questionList = new ArrayList<Question>(collectionQuestions);
                // Change order of questions
                Collections.shuffle(questionList);
                Question currentQuestion = new Question();

                isClickable = true;
                numQuestion++;

                for (Question q : questionList) {
                    if (!isInlist(q, questionsOk)) {
                        currentQuestion = q;
                        questionInProgress = q;
                        questionsOk.add(q);
                        break;
                    }
                }

                if (currentQuestion == null || currentQuestion.getLabel() == null) {
                    Intent myIntent = new Intent(StartQuizz.this, EndGame.class);
                    myIntent.putExtra(GOODQUESTIONS, goodQuestions);
                    myIntent.putExtra(BADQUESTIONS, badQuestions);
                    myIntent.putExtra(QUIZZ, quizz);
                    myIntent.putExtra(USER, connectedUser);
                    myIntent.putExtra(TIMER, timerInt);
                    myIntent.putExtra(EVALUATIONID, evaluationId);
                    startActivity(myIntent);
                    finish();
                } else {
                    this.initBackgroundButtons();

                    // Change name of the question
                    setNameQuestionQuizz(currentQuestion.getLabel());

                    // Buttons - proposals responses of the quizz
                    Collection<Response> responses = currentQuestion.getReponses();

                    // We shuffle responses (changer order)
                    listResponses.clear();
                    listResponses = new ArrayList<Response>(responses);
                    Collections.shuffle(listResponses);

                    // To iterate
                    Iterator<Response> responsesItr = listResponses.iterator();
                    if(timerInt != -1){
                        timerQuizzValue.setText(String.valueOf(timerInt));
                    }

                    listButtonsView.clear();
                    setProposalsResponsesQuizz(responsesItr, currentQuestion);

                    // Themes of the question
                    Collection<Theme> themes = currentQuestion.getThemes();
                    Iterator<Theme> themesItr = themes.iterator();

                    setThemesQuestion(themesItr);

                    nbClicksButton = 0;
                    if(timerInt != -1){
                        handler.postDelayed(runnable, TIMER_REFRESH);
                    }

                }
            }

            /**
             * Private function used to set the default background color to buttons
             */
            private void initBackgroundButtons() {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
                params.leftMargin = 10;
                params.rightMargin = 10;
                params.topMargin = 10;
                params.bottomMargin = 10;

                Button response1 = (Button) findViewById(R.id.Response1);
                response1.setBackground(bgButton);
                response1.setTextColor(Color.BLACK);
                response1.setLayoutParams(params);

                Button response2 = (Button) findViewById(R.id.Response2);
                response2.setBackground(bgButton);
                response2.setTextColor(Color.BLACK);
                response2.setLayoutParams(params);

                Button response3 = (Button) findViewById(R.id.Response3);
                response3.setBackground(bgButton);
                response3.setTextColor(Color.BLACK);
                response3.setLayoutParams(params);

                Button response4 = (Button) findViewById(R.id.Response4);
                response4.setBackground(bgButton);
                response4.setTextColor(Color.BLACK);
                response4.setLayoutParams(params);
            }

            /**
             * Private function used to set background of the good answer button if error
             */
            private void checkGoodResponseIfError() {
                int i = 1;
                for (Response r : listResponses) {
                    if (r.getValide()) {
                        Button response = null;
                        if (i == 1) {
                            response = (Button) findViewById(R.id.Response1);
                        } else if (i == 2) {
                            response = (Button) findViewById(R.id.Response2);
                        } else if (i == 3) {
                            response = (Button) findViewById(R.id.Response3);
                        } else {
                            response = (Button) findViewById(R.id.Response4);
                        }
                        response.setBackground(getResources().getDrawable(R.drawable.border_success));
                        response.setTextColor(Color.WHITE);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
                        params.leftMargin = 10;
                        params.rightMargin = 10;
                        params.topMargin = 10;
                        params.bottomMargin = 10;
                        response.setLayoutParams(params);
                    }
                    i++;
                }
            }
        });
    }

    /**
     * Private function used to know if a Question is in a List<Question>
     *
     * @return boolean true / false
     */
    private boolean isInlist(Question q, List<Question> listQ) {
        for (Question ques : listQ) {
            if (ques.equals(q)) {
                return true;
            }
        }
        return false;
    }

    class ProcessTimerReceiver extends BroadcastReceiver {
        private TextView timerQuizzValue;

        @Override
        public void onReceive(Context context, Intent intent) {
            timerQuizzValue = (TextView) findViewById(R.id.timerQuizzValue);
            int oldTimer = Integer.parseInt(timerQuizzValue.getText().toString());
            timerQuizzValue.setText(String.valueOf(oldTimer--));
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timerQuizzValue = (TextView) findViewById(R.id.timerQuizzValue);
            timerInt--;
            timerQuizzValue.setText(String.valueOf(timerInt));
            if (timerInt > 0) {
                handler.postDelayed(this, TIMER_REFRESH);
            } else {
                if (questionInProgress != null) {
                    List<Response> lResponses = (ArrayList<Response>) questionInProgress.getReponses();
                    if (lResponses != null && lResponses.size() != 0) {
                        String rightAnswer = "";
                        for(Response r: lResponses){
                            if(r.getValide()){
                                rightAnswer = r.getLabel();
                                break;
                            }
                        }
                        boolean firstWrongQuestion = false;
                        for (Response r : lResponses) {
                            if (r.getValide() == false) {
                                for (Button b : listButtonsView) {
                                    //Search right answer
                                    if (!b.getText().toString().equalsIgnoreCase(rightAnswer) && !firstWrongQuestion) {
                                        System.out.println(b.getText());
                                        b.performClick();
                                        firstWrongQuestion = true;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
    };
}