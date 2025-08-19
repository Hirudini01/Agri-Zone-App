package com.s23010372.agrizone;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.view.LayoutInflater;
import android.content.Context;

public class CommunityActivity extends Activity {
    private static final int PICK_IMAGE_REQUEST = 101;
    private EditText etPostContent;
    private Button btnAddPhoto, btnPost;
    private ImageView ivSelectedImage;
    private LinearLayout postsContainer;
    private ProgressBar progressBar;
    private LinearLayout emptyState;
    private Uri selectedImageUri = null;
    private DatabaseActivity db;
    private String currentUsername = "Anonymous";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        db = new DatabaseActivity(this);

        // Get current user from intent
        currentUsername = getIntent().getStringExtra("username");
        if (currentUsername == null || currentUsername.isEmpty()) {
            currentUsername = "Anonymous";
        }

        initializeViews();
        setupClickListeners();
        updateUserStats();
        loadPosts();
    }

    private void initializeViews() {
        etPostContent = findViewById(R.id.et_post_content);
        btnAddPhoto = findViewById(R.id.btn_add_photo);
        btnPost = findViewById(R.id.btn_post);
        ivSelectedImage = findViewById(R.id.iv_selected_image);
        postsContainer = findViewById(R.id.posts_container);
        progressBar = findViewById(R.id.progress_bar);
        emptyState = findViewById(R.id.empty_state);

        // Null checks for critical views
        if (etPostContent == null) throw new RuntimeException("et_post_content not found");
        if (btnAddPhoto == null) throw new RuntimeException("btn_add_photo not found");
        if (btnPost == null) throw new RuntimeException("btn_post not found");
        if (postsContainer == null) throw new RuntimeException("posts_container not found");
    }

    private void setupClickListeners() {
        // Back button functionality
        ImageView backButton = findViewById(R.id.back_button);
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        btnAddPhoto.setOnClickListener(v -> pickImage());
        btnPost.setOnClickListener(v -> postProblem());
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            ivSelectedImage.setVisibility(View.VISIBLE);
            ivSelectedImage.setImageURI(selectedImageUri);
        }
    }

    private void postProblem() {
        String content = etPostContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            etPostContent.setError("Please describe your problem");
            return;
        }
        String imagePath = "";
        if (selectedImageUri != null) {
            imagePath = selectedImageUri.toString();
        }
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        long result = db.addCommunityPost(currentUsername, content, imagePath, timestamp);
        if (result != -1) {
            etPostContent.setText("");
            ivSelectedImage.setVisibility(View.GONE);
            selectedImageUri = null;
            updateUserStats();
            loadPosts();
            Toast.makeText(this, "Post shared successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to share post. Try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUserStats() {
        TextView userStatsText = findViewById(R.id.user_stats);
        if (userStatsText != null && !currentUsername.equals("Anonymous")) {
            int postCount = db.getUserPostCount(currentUsername);
            int commentCount = db.getUserCommentCount(currentUsername);
            userStatsText.setText("Posts: " + postCount + " • Replies: " + commentCount);
            userStatsText.setVisibility(View.VISIBLE);
        }
    }

    private void loadPosts() {
        postsContainer.removeAllViews();
        Cursor cursor = db.getAllCommunityPosts();
        if (cursor.getCount() == 0) {
            emptyState.setVisibility(View.VISIBLE);
            postsContainer.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            postsContainer.setVisibility(View.VISIBLE);
            while (cursor.moveToNext()) {
                long postId = cursor.getLong(cursor.getColumnIndex(DatabaseActivity.COL_POST_ID));
                String user = cursor.getString(cursor.getColumnIndex(DatabaseActivity.COL_POST_USER));
                String content = cursor.getString(cursor.getColumnIndex(DatabaseActivity.COL_POST_CONTENT));
                String imagePath = cursor.getString(cursor.getColumnIndex(DatabaseActivity.COL_POST_IMAGE));
                String timestamp = cursor.getString(cursor.getColumnIndex(DatabaseActivity.COL_POST_TIMESTAMP));
                View postView = createPostView(postId, user, content, imagePath, timestamp);
                postsContainer.addView(postView);
            }
        }
        cursor.close();
    }

    private View createPostView(long postId, String user, String content, String imagePath, String timestamp) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CardView card = new CardView(this);
        card.setRadius(16);
        card.setCardElevation(8);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(24, 24, 24, 24);

        TextView tvUser = new TextView(this);
        tvUser.setText(user + " • " + timestamp);
        tvUser.setTextSize(14);
        tvUser.setTextColor(getResources().getColor(R.color.hint_text));
        layout.addView(tvUser);

        TextView tvContent = new TextView(this);
        tvContent.setText(content);
        tvContent.setTextSize(16);
        tvContent.setTextColor(getResources().getColor(R.color.text_white));
        layout.addView(tvContent);

        if (!TextUtils.isEmpty(imagePath)) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 400));
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setBackgroundResource(R.drawable.edittext_background);
            iv.setPadding(0, 16, 0, 16);
            iv.setImageURI(Uri.parse(imagePath));
            layout.addView(iv);
        }

        // Comments section
        LinearLayout commentsLayout = new LinearLayout(this);
        commentsLayout.setOrientation(LinearLayout.VERTICAL);
        commentsLayout.setPadding(0, 16, 0, 0);

        Cursor commentsCursor = db.getCommentsForPost(postId);
        while (commentsCursor.moveToNext()) {
            String commentUser = commentsCursor.getString(commentsCursor.getColumnIndex(DatabaseActivity.COL_COMMENT_USER));
            String commentContent = commentsCursor.getString(commentsCursor.getColumnIndex(DatabaseActivity.COL_COMMENT_CONTENT));
            String commentTimestamp = commentsCursor.getString(commentsCursor.getColumnIndex(DatabaseActivity.COL_COMMENT_TIMESTAMP));
            TextView tvComment = new TextView(this);
            tvComment.setText(commentUser + ": " + commentContent + " (" + commentTimestamp + ")");
            tvComment.setTextSize(13);
            tvComment.setTextColor(getResources().getColor(R.color.hint_text));
            commentsLayout.addView(tvComment);
        }
        commentsCursor.close();

        // Add comment input
        LinearLayout addCommentLayout = new LinearLayout(this);
        addCommentLayout.setOrientation(LinearLayout.HORIZONTAL);
        addCommentLayout.setPadding(0, 8, 0, 0);

        EditText etComment = new EditText(this);
        etComment.setHint("Write a solution...");
        etComment.setTextColor(getResources().getColor(R.color.text_white));
        etComment.setHintTextColor(getResources().getColor(R.color.hint_text));
        etComment.setBackgroundResource(R.drawable.edittext_background);
        etComment.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        Button btnSend = new Button(this);
        btnSend.setText("Reply");
        btnSend.setBackgroundColor(getResources().getColor(R.color.button_green));
        btnSend.setTextColor(getResources().getColor(R.color.text_white));
        btnSend.setOnClickListener(v -> {
            String commentText = etComment.getText().toString().trim();
            if (!TextUtils.isEmpty(commentText)) {
                String commentTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                long result = db.addCommunityComment(postId, currentUsername, commentText, commentTimestamp);
                if (result != -1) {
                    updateUserStats();
                    loadPosts();
                    Toast.makeText(CommunityActivity.this, "Reply posted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CommunityActivity.this, "Failed to post reply.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addCommentLayout.addView(etComment);
        addCommentLayout.addView(btnSend);

        layout.addView(commentsLayout);
        layout.addView(addCommentLayout);

        card.addView(layout);
        return card;
    }
}
