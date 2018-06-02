package com.my_project.test_view_custom.interfaces;

import com.my_project.test_view_custom.view.CustomerPuzzleCodeView;

/**
 * Created by com_c on 2017/12/27.
 */

public interface OnCaptchaMatchCallback {
    void matchSuccess(CustomerPuzzleCodeView customerPuzzleCodeView);
    void matchFailed(CustomerPuzzleCodeView customerPuzzleCodeView);
}
