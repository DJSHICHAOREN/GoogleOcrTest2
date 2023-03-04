package com.example.djshichaoren.googleocrtest2.core.recogonize;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.annotation.StringRes;

import com.example.djshichaoren.googleocrtest2.R;
import com.example.djshichaoren.googleocrtest2.core.recogonize.paddle.OcrResultModel;
import com.example.djshichaoren.googleocrtest2.core.recogonize.paddle.Predictor;
import com.example.djshichaoren.googleocrtest2.core.recogonize.paddle.Utils;
import com.example.djshichaoren.googleocrtest2.models.RecognitionResult;

import java.util.ArrayList;
import java.util.List;

public class PaddleOcrImpl implements OcrProxy {

    // Model settings of object detection
    protected String modelPath = "";
    protected String labelPath = "";
    protected String imagePath = "";
    protected int cpuThreadNum = 1;
    protected String cpuPowerMode = "";
    protected String inputColorFormat = "";
    protected long[] inputShape = new long[]{};
    protected float[] inputMean = new float[]{};
    protected float[] inputStd = new float[]{};
    protected float scoreThreshold = 0.1f;
    private String currentPhotoPath;

    protected Predictor predictor = new Predictor();

    private Context mContext;
    public static PaddleOcrImpl mPaddleOcrImpl;

    public PaddleOcrImpl(Context context) {
        mContext = context;
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                // 初始化模型
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                boolean settingsChanged = false;
                String model_path = sharedPreferences.getString(getString(R.string.MODEL_PATH_KEY),
                        getString(R.string.MODEL_PATH_DEFAULT));
                String label_path = sharedPreferences.getString(getString(R.string.LABEL_PATH_KEY),
                        getString(R.string.LABEL_PATH_DEFAULT));
                String image_path = sharedPreferences.getString(getString(R.string.IMAGE_PATH_KEY),
                        getString(R.string.IMAGE_PATH_DEFAULT));
                settingsChanged |= !model_path.equalsIgnoreCase(modelPath);
                settingsChanged |= !label_path.equalsIgnoreCase(labelPath);
                settingsChanged |= !image_path.equalsIgnoreCase(imagePath);
                int cpu_thread_num = Integer.parseInt(sharedPreferences.getString(getString(R.string.CPU_THREAD_NUM_KEY),
                        getString(R.string.CPU_THREAD_NUM_DEFAULT)));
                settingsChanged |= cpu_thread_num != cpuThreadNum;
                String cpu_power_mode =
                        sharedPreferences.getString(getString(R.string.CPU_POWER_MODE_KEY),
                                getString(R.string.CPU_POWER_MODE_DEFAULT));
                settingsChanged |= !cpu_power_mode.equalsIgnoreCase(cpuPowerMode);
                String input_color_format =
                        sharedPreferences.getString(getString(R.string.INPUT_COLOR_FORMAT_KEY),
                                getString(R.string.INPUT_COLOR_FORMAT_DEFAULT));
                settingsChanged |= !input_color_format.equalsIgnoreCase(inputColorFormat);
                long[] input_shape =
                        Utils.parseLongsFromString(sharedPreferences.getString(getString(R.string.INPUT_SHAPE_KEY),
                                getString(R.string.INPUT_SHAPE_DEFAULT)), ",");
                float[] input_mean =
                        Utils.parseFloatsFromString(sharedPreferences.getString(getString(R.string.INPUT_MEAN_KEY),
                                getString(R.string.INPUT_MEAN_DEFAULT)), ",");
                float[] input_std =
                        Utils.parseFloatsFromString(sharedPreferences.getString(getString(R.string.INPUT_STD_KEY)
                                , getString(R.string.INPUT_STD_DEFAULT)), ",");
                settingsChanged |= input_shape.length != inputShape.length;
                settingsChanged |= input_mean.length != inputMean.length;
                settingsChanged |= input_std.length != inputStd.length;
                if (!settingsChanged) {
                    for (int i = 0; i < input_shape.length; i++) {
                        settingsChanged |= input_shape[i] != inputShape[i];
                    }
                    for (int i = 0; i < input_mean.length; i++) {
                        settingsChanged |= input_mean[i] != inputMean[i];
                    }
                    for (int i = 0; i < input_std.length; i++) {
                        settingsChanged |= input_std[i] != inputStd[i];
                    }
                }
                float score_threshold =
                        Float.parseFloat(sharedPreferences.getString(getString(R.string.SCORE_THRESHOLD_KEY),
                                getString(R.string.SCORE_THRESHOLD_DEFAULT)));
                settingsChanged |= scoreThreshold != score_threshold;
                if (settingsChanged) {
                    modelPath = model_path;
                    labelPath = label_path;
                    imagePath = image_path;
                    cpuThreadNum = cpu_thread_num;
                    cpuPowerMode = cpu_power_mode;
                    inputColorFormat = input_color_format;
                    inputShape = input_shape;
                    inputMean = input_mean;
                    inputStd = input_std;
                    scoreThreshold = score_threshold;
                    // Reload model if configure has been changed
                    loadModel();
                }


            }
        });

    }

    public void loadModel() {
        predictor.init(mContext, modelPath, labelPath, cpuThreadNum,
                cpuPowerMode,
                inputColorFormat,
                inputShape, inputMean,
                inputStd, scoreThreshold);
    }


    @Override
    public ArrayList<RecognitionResult> recognize(Bitmap image) {
        if (image != null && predictor.isLoaded()) {
            predictor.setInputImage(image);
            if (predictor.runModel()) {
                ArrayList<RecognitionResult> recognitionResultArrayList = new ArrayList<>();

                ArrayList<OcrResultModel> ocrResultModelArrayList = predictor.getOutputObjectListResult();
                for (OcrResultModel ocrResultModel : ocrResultModelArrayList) {
                    List<Point> points = ocrResultModel.getPoints();
                    if (points != null && points.size() > 3) {
                        RecognitionResult recognitionResult
                                = new RecognitionResult(points.get(0).y, points.get(0).x
                                        , points.get(2).x - points.get(2).x
                                        , points.get(2).y - points.get(2).y
                                        , ocrResultModel.getLabel());
                        recognitionResultArrayList.add(recognitionResult);
                    }

                }

                return recognitionResultArrayList;
            }
        }
        return null;
    }

    public final String getString(@StringRes int resId) {
        return mContext.getString(resId);
    }


    public static PaddleOcrImpl newInstance(Context context){
        if(mPaddleOcrImpl == null){
            mPaddleOcrImpl = new PaddleOcrImpl(context);
        }
        return mPaddleOcrImpl;
    }
}
