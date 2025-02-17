/*

 This is the source code of exteraGram for Android.

 We do not and cannot prevent the use of our code,
 but be respectful and credit the original author.

 Copyright @immat0x1, 2022.

*/

package uz.unnarsx.cherrygram.ota;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RLottieImageView;

import java.util.Objects;

import uz.unnarsx.cherrygram.CherrygramConfig;
import uz.unnarsx.cherrygram.prefviews.TextCheckWithIconCell;
import uz.unnarsx.extras.CherrygramExtras;

public class UpdaterBottomSheet extends BottomSheet {

    private RLottieImageView imageView;
    private TextView changelogTextView;

    private boolean animationInProgress;

    private boolean isTranslated = false;
    private CharSequence translatedC;

    @SuppressLint("AppCompatCustomView")
    public UpdaterBottomSheet(Context context, boolean available, String... args) {
        // args = {version, changelog, size, downloadUrl, uploadDate}
        super(context, false);
        setOpenNoDelay(true);
        fixNavigationBar();

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        FrameLayout header = new FrameLayout(context);
        linearLayout.addView(header, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 21, 10, 0, 10));

        if (available) {
            imageView = new RLottieImageView(context);
            imageView.setOnClickListener(v -> {
                if (!imageView.isPlaying() && imageView.getAnimatedDrawable() != null) {
                    imageView.getAnimatedDrawable().setCurrentFrame(0);
                    imageView.playAnimation();
                }
            });
            imageView.setAnimation(R.raw.wallet_congrats, 60, 60, new int[]{0x000000, 0x000000});
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            header.addView(imageView, LayoutHelper.createFrame(60, 60, Gravity.LEFT | Gravity.CENTER_VERTICAL));
        }

        SimpleTextView nameView = new SimpleTextView(context);
        nameView.setTextSize(20);
        nameView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        nameView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        nameView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        nameView.setText(LocaleController.getString("CG_AppName", R.string.CG_AppName));
        header.addView(nameView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 30, Gravity.LEFT, available ? 75 : 0, 5, 0, 0));

        SimpleTextView timeView = new SimpleTextView(context);
        timeView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
        timeView.setTextSize(13);
        timeView.setTypeface(AndroidUtilities.getTypeface("fonts/rregular.ttf"));
        timeView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        timeView.setText(available ? args[4] : LocaleController.getString("UP_LastCheck", R.string.UP_LastCheck) + ": " + LocaleController.formatDateTime(CherrygramConfig.INSTANCE.getLastUpdateCheckTime() / 1000));
        header.addView(timeView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 20, Gravity.LEFT, available ? 75 : 0, 35, 0, 0));

        TextCell version = new TextCell(context);
        version.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_listSelector), 100, 0));
        if (available) {
            version.setTextAndValueAndIcon(LocaleController.getString("UP_Version", R.string.UP_Version), args[0].replaceAll("v|-beta", ""), R.drawable.info_outline_28, true);
        } else {
            version.setTextAndValueAndIcon(LocaleController.getString("UP_CurrentVersion", R.string.UP_CurrentVersion), CherrygramExtras.INSTANCE.getCG_VERSION(), R.drawable.info_outline_28, false);
        }
        version.setOnClickListener(v -> copyText(version.getTextView().getText() + ": " + version.getValueTextView().getText()));
        linearLayout.addView(version);


        if (available) {
            TextCell size = new TextCell(context);
            size.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_listSelector), 100, 0));
            size.setTextAndValueAndIcon(LocaleController.getString("UP_UpdateSize", R.string.UP_UpdateSize), args[2], R.drawable.document_outline_28, true);
            size.setOnClickListener(v -> copyText(size.getTextView().getText() + ": " + size.getValueTextView().getText()));
            linearLayout.addView(size);

            TextCell changelog = new TextCell(context);
            changelog.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_listSelector), 100, 0));
            changelog.setTextAndIcon(LocaleController.getString("UP_Changelog", R.string.UP_Changelog), R.drawable.grid_square_outline_28, false);
            changelog.setOnClickListener(v -> copyText(changelog.getTextView().getText() + "\n" + (isTranslated ? translatedC : UpdaterUtils.replaceTags(args[1]))));
            linearLayout.addView(changelog);

            changelogTextView = new TextView(context) {
                @Override
                protected void onDraw(Canvas canvas) {
                    super.onDraw(canvas);
                    canvas.drawLine(0, getMeasuredHeight() - 1, getMeasuredWidth(), getMeasuredHeight() - 1, Theme.dividerPaint);
                }
            };
            changelogTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
            changelogTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            changelogTextView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
            changelogTextView.setLinkTextColor(Theme.getColor(Theme.key_dialogTextLink));
            changelogTextView.setText(UpdaterUtils.replaceTags(args[1]));
            changelogTextView.setPadding(AndroidUtilities.dp(21), 0, AndroidUtilities.dp(21), AndroidUtilities.dp(10));
            changelogTextView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            changelogTextView.setOnClickListener(v -> UpdaterUtils.translate(args[1], (String translated) -> {
                translatedC = translated;
                animateChangelog(UpdaterUtils.replaceTags(isTranslated ? args[1] : (String) translatedC));
                isTranslated ^= true;
            }, () -> {}));
            linearLayout.addView(changelogTextView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

            TextView doneButton = new TextView(context);
            doneButton.setLines(1);
            doneButton.setSingleLine(true);
            doneButton.setEllipsize(TextUtils.TruncateAt.END);
            doneButton.setGravity(Gravity.CENTER);
            doneButton.setTextColor(Theme.getColor(Theme.key_featuredStickers_buttonText));
            doneButton.setBackground(Theme.AdaptiveRipple.filledRect(Theme.getColor(Theme.key_featuredStickers_addButton), 6));
            doneButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            doneButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            doneButton.setText(LocaleController.getString("AppUpdateDownloadNow", R.string.AppUpdateDownloadNow));
            doneButton.setOnClickListener(v -> {
                UpdaterUtils.downloadApk(context, args[3], "Cherrygram " + args[0]);
                dismiss();
            });
            linearLayout.addView(doneButton, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 48, 0, 16, 15, 16, 5));

            TextView scheduleButton = new TextView(context);
            scheduleButton.setLines(1);
            scheduleButton.setSingleLine(true);
            scheduleButton.setEllipsize(TextUtils.TruncateAt.END);
            scheduleButton.setGravity(Gravity.CENTER);
            scheduleButton.setTextColor(Theme.getColor(Theme.key_featuredStickers_addButton));
            scheduleButton.setBackground(null);
            scheduleButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            scheduleButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            scheduleButton.setText(LocaleController.getString("AppUpdateRemindMeLater", R.string.AppUpdateRemindMeLater));
            scheduleButton.setOnClickListener(v -> {
                CherrygramConfig.INSTANCE.setUpdateScheduleTimestamp(System.currentTimeMillis());
                dismiss();
            });
            linearLayout.addView(scheduleButton, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 48, 0, 16, 1, 16, 0));
        } else {
            /*final String btype = BuildVars.isBetaApp() ? LocaleController.getString("BTBeta", R.string.BTBeta) : LocaleController.getString("BTRelease", R.string.BTRelease);
            TextCell buildType = new TextCell(context);
            buildType.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_listSelector), 100, 0));
            buildType.setTextAndValueAndIcon(LocaleController.getString("BuildType", R.string.BuildType), btype, R.drawable.msg_customize, false);
            buildType.setOnClickListener(v -> copyText(buildType.getTextView().getText() + ": " + buildType.getValueTextView().getText()));
            linearLayout.addView(buildType);*/

            TextCheckWithIconCell checkOnLaunch = new TextCheckWithIconCell(context);
            checkOnLaunch.setEnabled(true, null);
            checkOnLaunch.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_listSelector), 100, 0));
            checkOnLaunch.setTextAndCheckAndIcon(LocaleController.getString("UP_Auto_OTA", R.string.UP_Auto_OTA), R.drawable.switch_outline_28, CherrygramConfig.INSTANCE.getAutoOTA(), false);
            checkOnLaunch.setOnClickListener(v -> {
                CherrygramConfig.INSTANCE.toggleAutoOTA();
                checkOnLaunch.setChecked(!checkOnLaunch.isChecked());
            });
            linearLayout.addView(checkOnLaunch);

            TextCell clearUpdates = new TextCell(context) {
                @Override
                protected void onDraw(Canvas canvas) {
                    super.onDraw(canvas);
                    canvas.drawLine(0, getMeasuredHeight() - 1, getMeasuredWidth(), getMeasuredHeight() - 1, Theme.dividerPaint);
                }
            };
            clearUpdates.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_listSelector), 100, 0));
            clearUpdates.setTextAndIcon(LocaleController.getString("UP_ClearUpdatesCache", R.string.UP_ClearUpdatesCache), R.drawable.clear_data_outline_28, false);
            clearUpdates.setOnClickListener(v -> {
                if (UpdaterUtils.getOtaDirSize().replaceAll("[^0-9]+", "").equals("0")) {
                    BulletinFactory.of(getContainer(), null).createErrorBulletin(LocaleController.getString("UP_NothingToClear", R.string.UP_NothingToClear)).show();
                } else {
                    BulletinFactory.of(getContainer(), null).createErrorBulletin(LocaleController.formatString("UP_ClearedUpdatesCache", R.string.UP_ClearedUpdatesCache, UpdaterUtils.getOtaDirSize())).show();
                    UpdaterUtils.cleanOtaDir();
                }
            });
            linearLayout.addView(clearUpdates);

            TextView checkUpdatesButton = new TextView(context);
            checkUpdatesButton.setLines(1);
            checkUpdatesButton.setSingleLine(true);
            checkUpdatesButton.setEllipsize(TextUtils.TruncateAt.END);
            checkUpdatesButton.setGravity(Gravity.CENTER);
            checkUpdatesButton.setTextColor(Theme.getColor(Theme.key_featuredStickers_buttonText));
            checkUpdatesButton.setBackground(Theme.AdaptiveRipple.filledRect(Theme.getColor(Theme.key_featuredStickers_addButton), 6));
            checkUpdatesButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            checkUpdatesButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            spannableStringBuilder.append(".  ").append(LocaleController.getString("UP_CheckForUpdates", R.string.UP_CheckForUpdates));
            spannableStringBuilder.setSpan(new ColoredImageSpan(Objects.requireNonNull(ContextCompat.getDrawable(getContext(), R.drawable.sync_outline_28))), 0, 1, 0);
            checkUpdatesButton.setText(spannableStringBuilder);
            checkUpdatesButton.setOnClickListener(v -> UpdaterUtils.checkUpdates(context, true, () -> {
                BulletinFactory.of(getContainer(), null).createErrorBulletin(LocaleController.getString("UP_Not_Found", R.string.UP_Not_Found)).show();
                timeView.setText(LocaleController.getString("UP_LastCheck", R.string.UP_LastCheck) + ": " + LocaleController.formatDateTime(CherrygramConfig.INSTANCE.getLastUpdateCheckTime() / 1000));
            }, this::dismiss));
            linearLayout.addView(checkUpdatesButton, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 48, 0, 16, 15, 16, 16));
        }

        ScrollView scrollView = new ScrollView(context);
        scrollView.addView(linearLayout);
        setCustomView(scrollView);
    }

    private void animateChangelog(CharSequence text) {
        changelogTextView.setText(text);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(250);
        animatorSet.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(changelogTextView, View.ALPHA, 0.0f, 1.0f),
                ObjectAnimator.ofFloat(changelogTextView, View.TRANSLATION_Y, AndroidUtilities.dp(12), 0)
        );
        animatorSet.start();
    }

    private void copyText(CharSequence text) {
        AndroidUtilities.addToClipboard(text);
        BulletinFactory.of(getContainer(), null).createCopyBulletin(LocaleController.getString("TextCopied", R.string.TextCopied)).show();
    }

    @Override
    public void show() {
        super.show();
        if (imageView != null) {
            imageView.playAnimation();
        }
    }
}
