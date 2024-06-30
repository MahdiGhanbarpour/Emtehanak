package ir.mahdighanbarpour.khwarazmiapp.utils

// The keys used to transfer data in the application
const val SEND_SELECTED_ROLE_TO_LOGIN_OTP_FRAGMENT_KEY = "send_selected_role_to_login_otp_fragment"
const val SEND_ENTERED_PHONE_NUMBER_TO_OTP_PAGE_KEY = "send_entered_phone_number_to_otp_page"

const val SEND_SELECTED_ROLE_TO_REGISTER_FRAGMENT_KEY = "send_selected_role_to_register_fragment"
const val SEND_ENTERED_PHONE_NUMBER_TO_REG_PAGE_KEY = "send_entered_phone_number_to_reg_page"

const val SEND_SELECTED_ROLE_TO_HELP_BOTTOM_SHEET_KEY = "send_selected_role_to_help_bottom_sheet"

const val SEND_PAGE_NAME_TO_FREQUENTLY_QUESTIONS_PAGE_KEY =
    "send_page_name_to_frequently_questions_page"

const val SEND_SELECTED_EXAM_TO_EXAM_DETAIL_PAGE_KEY = "send_selected_exam_to_exam_page"

const val SEND_SELECTED_EXAM_QUESTION_TO_EXAM_MAIN_PAGE_KEY =
    "send_selected_exam_question_to_exam_main_page"

const val SEND_CORRECT_ANSWERS_COUNT_TO_EXAM_RESULT_BOTTOM_SHEET_KEY =
    "send_correct_answers_count_to_exam_result_bottom_sheet"
const val SEND_INCORRECT_ANSWERS_COUNT_TO_EXAM_RESULT_BOTTOM_SHEET_KEY =
    "send_incorrect_answers_count_to_exam_result_bottom_sheet"
const val SEND_UNANSWERED_COUNT_TO_EXAM_RESULT_BOTTOM_SHEET_KEY =
    "send_unanswered_count_to_exam_result_bottom_sheet"

const val SEND_ENTERED_NAME_TO_GRADES_REGISTER_FRAGMENT_KEY =
    "send_entered_name_to_grades_register_fragment"
const val SEND_ENTERED_PHONE_NUMBER_TO_GRADES_REGISTER_FRAGMENT_KEY =
    "send_entered_phone_number_to_grades_register_fragment"
const val SEND_ENTERED_BIRTHDAY_TO_GRADES_REGISTER_FRAGMENT_KEY =
    "send_entered_birthday_to_grades_register_fragment"
const val SEND_ENTERED_STUDY_FIELD_TO_GRADES_REGISTER_FRAGMENT_KEY =
    "send_entered_study_field_to_grades_register_fragment"
const val SEND_ENTERED_ACTIVITY_YEAR_TO_GRADES_REGISTER_FRAGMENT_KEY =
    "send_entered_activity_year_to_grades_register_fragment"

const val SEND_CREATED_EXAM_ID_TO_ADD_EXAM_QUESTION_PAGE_KEY =
    "send_created_exam_id_to_add_exam_question_page"
const val SEND_CREATED_EXAM_GRADE_TO_ADD_EXAM_QUESTION_PAGE_KEY =
    "send_created_exam_grade_to_add_exam_question_page"

// Constants of user roles
const val TEACHER = "teacher"
const val STUDENT = "student"

// Page constants (used in getting the list of frequently asked questions)
const val LOGIN_MAIN = "login_main"
const val LOGIN_OTP = "login_otp"
const val REGISTER_MAIN = "register_main"
const val REGISTER_TEACHER = "register_teacher"
const val STUDENT_MAIN = "student_main"
const val TEACHER_MAIN = "teacher_main"
const val EXAM_DETAIL = "exam_detail"
const val EXAM_MAIN = "exam_main"

// Constants used in Shared Preferences
const val IS_USER_LOGGED_IN = "is_user_logged_in"
const val USER_FULL_NAME = "user_full_name"
const val USER_PHONE_NUM = "user_phone_num"
const val USER_GRADE = "user_grade"
const val USER_ROLE = "user_role"
const val USER_STUDY_FIELD = "user_study_field"

// Constants related to connection with the server
const val BASE_URL = "https://persiadev.net/khwarizmi/"
const val MEDIA_BASE_URL = "https://persiadev.net/khwarizmi"