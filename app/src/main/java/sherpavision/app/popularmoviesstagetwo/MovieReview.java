package sherpavision.app.popularmoviesstagetwo;

import android.os.Bundle;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;

public class MovieReview extends AppCompatActivity {
    @BindView(R2.id.review_content)
    TextView mReviewContentView;
    @BindView(R2.id.review_author)
    TextView mReviewAuthorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_review);
        ButterKnife.bind(this);
        Intent callingIntent = getIntent();
        mReviewAuthorView.setText(callingIntent.getStringExtra("reviewAuthor"));
        mReviewContentView.setText(callingIntent.getStringExtra("reviewContent"));
    }
}
