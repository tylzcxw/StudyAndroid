package tylz.study.studyexample.keyboard;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import tylz.study.R;

public class KeyboardUtil {
    private KeyboardView keyboardView;
    private Keyboard k1;// 字母键盘
    private Keyboard k2;// 数字键盘
    private Keyboard k3;// 符号键盘
    private boolean isNum = false;// 是否数据键盘
    private boolean isUpper = false;// 是否大写
    private boolean isSymbol = false;// 是否符号

    private static final int SYMBOL_CODE = -7;//符号键盘
    private static final int ELLIPSES_CODE = -8;//省略号
    private static final int CHINESE_HORIZONTAL_LINE_CODE = -9;//中文横线
    private static final int SMILING_FACE_CODE = -10;//笑脸
    private static final int LEFT_CODE = -11;//中文横线
    private static final int RIGHT_CODE = -12;//中文横线
    private static final int HEE_CODE = -13;//哈哈
    private static final int AWKWARD_CODE = -14;//尴尬

    private ViewGroup rootView;
    private EditText ed;


    private KeyboardUtil(Activity activity, EditText edit) {
        this.ed = edit;
        k1 = new Keyboard(activity, R.xml.letter);
        k2 = new Keyboard(activity, R.xml.number);
        k3 = new Keyboard(activity, R.xml.symbol);


        keyboardView = new KeyboardView(activity, null);
        keyboardView.setKeyboard(k1);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(onKeyboardActionListener);
        ed.setOnTouchListener(mOnTouchListener);
        ed.addTextChangedListener(mTextWatcher);
        rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);

    }
    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            ed.setSelection(ed.length());
        }
    };
    View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int inputType = ed.getInputType();
            ed.setInputType(InputType.TYPE_NULL);
            ed.onTouchEvent(event);
            ed.setInputType(inputType);
            return true;
        }
    };
    OnKeyboardActionListener onKeyboardActionListener = new OnKeyboardActionListener() {
        /**
         * 当用户快速将手指从下向上移动时调用。
         */
        @Override
        public void swipeUp() {
        }
        /**
         * 当用户从左向右快速移动手指时调用。
         */
        @Override
        public void swipeRight() {
        }
        /**
         * 当用户从右向左快速移动手指时调用。
         */
        @Override
        public void swipeLeft() {
        }
        /**
         * 当用户从上到下快速移动手指时调用。
         */
        @Override
        public void swipeDown() {
        }
        /**
         * 向侦听器发送一系列字符。
         * @param text 要显示的字符序列。
         */
        @Override
        public void onText(CharSequence text) {
        }
        /**
         * 当用户释放键时调用。 这是在调用onKey之后。
         * 对于重复的键，此键仅调用一次。
         * @param primaryCode 被释放的键的unicode
         */
        @Override
        public void onRelease(int primaryCode) {
        }
        /**
         * 当用户按下一个键时调用。 这是在调用onKey之前。
         * 对于重复的键，此键仅调用一次。
         * @param primaryCode 被按下的键的unicode。如果触摸不在有效范围内，值将为零。
         */
        @Override
        public void onPress(int primaryCode) {
        }
        /**
         * 发送一个按键到监听器
         * @ param primaryCode 这是按下的键
         * @ param keyCodes 所有可能的替代键的代码，
         */
        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = ed.getText();
            int start = ed.getSelectionStart();
            if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
                hideKeyboard();
            } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            } else if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 大小写切换
                isUpper = !isUpper;
                k1.setShifted(isUpper);
                keyboardView.invalidateAllKeys();
            } else if (primaryCode == SYMBOL_CODE) {// 符号键盘
                if (isSymbol) {
                    isSymbol = false;
                    keyboardView.setKeyboard(k2);
                } else {
                    isSymbol = true;
                    keyboardView.setKeyboard(k3);
                }
            } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {// 数字键盘切换
                if (isNum) {
                    isNum = false;
                    keyboardView.setKeyboard(k1);
                } else {
                    isNum = true;
                    keyboardView.setKeyboard(k2);
                }
            } else if (primaryCode == LEFT_CODE) { //向左
                if (start > 0) {
                    ed.setSelection(start - 1);
                }
            } else if (primaryCode == RIGHT_CODE) { // 向右
                if (start < ed.length()) {

                    ed.setSelection(start + 1);
                }
            } else if (primaryCode == ELLIPSES_CODE) { //省略号
                editable.insert(start, "...");
            } else if (primaryCode == CHINESE_HORIZONTAL_LINE_CODE) {
                editable.insert(start, "——");
            } else if (primaryCode == SMILING_FACE_CODE) {
                editable.insert(start, "^_^");
            } else if (primaryCode == HEE_CODE) {
                editable.insert(start, "^o^");
            } else if (primaryCode == AWKWARD_CODE) {
                editable.insert(start, ">_<");
            } else {
                String str = Character.toString((char) primaryCode);
                if (isWord(str)) {
                    if (isUpper) {
                        str = str.toUpperCase();
                    } else {
                        str = str.toLowerCase();
                    }
                }
                editable.insert(start, str);

            }
        }
    };

    private boolean isShow = false;

    public void showKeyboard() {
        if (!isShow) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            rootView.addView(keyboardView, layoutParams);
            isShow = true;
        }
    }

    private void hideKeyboard() {
        if (rootView != null && keyboardView != null && isShow) {
            isShow = false;
            rootView.removeView(keyboardView);
        }
        mInstance = null;
    }

    private boolean isWord(String str) {
        return str.matches("[a-zA-Z]");
    }

    private static KeyboardUtil mInstance;

    public static KeyboardUtil shared(Activity activity, EditText edit) {
        if (mInstance == null) {
            mInstance = new KeyboardUtil(activity, edit);
        }
        mInstance.ed = edit;
        return mInstance;
    }

}
