package com.shepherd.shep_blog.utils;

public final class RegexPattern {
    public static final String EMAIL = "^\\s*(?=.{1,254}$)(?!.*\\.\\.)(?!.*@.*@)(?!.*[_%+\\-]{2,})([a-zA-Z0-9](?:[a-zA-Z0-9._%+\\-]*[a-zA-Z0-9])?)@" +
            "([a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?)(\\.[a-zA-Z0-9-]+)*\\.([a-zA-Z]{2,24})\\s*$";

//    public static final String EMAIL = "^\\s*(?=.{1,254}$)(?!.*\\.\\.)(?!.*@.*@)(?!.*[_%+\\-]{2,})([a-zA-Z0-9](?:[a-zA-Z0-9._%+\\-]*[a-zA-Z0-9])?)@([a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?)\\.([a-zA-Z]{2,24})\\s*$";
    public static final String PASSWORD = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,20}$";
    public static final String PHONE_NUMBER = "^\\+?[0-9]{7,15}$";
//    public static final String PERSON_NAME = "^(?!.* {2})(?!.*'{2})(?!.*-{2})(?!.*\\.{2})[\\p{L}\\p{M}'\\-\\. ]{1,50}$";
//    public static final String PERSON_NAME = "^[A-Za-z]+(?:['- ][A-Za-z]+)*$";
    public static final String PERSON_NAME = "^[\\p{L}\\p{M}]+(?:['-. ][\\p{L}\\p{M}]+)*$";

    public static final String USER_NAME = "^(?![._])(?!.*[._]$)(?!.*\\.\\.)(?!.*__)(?!.*\\._)(?!.*_\\.)[a-zA-Z0-9._]{3,20}$";
    public static final String BOOK_TITLE = "^\\s*[a-zA-Z0-9](?:[a-zA-Z0-9\\s.,'!?()-]{0,98}[a-zA-Z0-9])?\\s*$";
    public static final String BOOK_AUTHOR = "^\\s*[a-zA-Z](?:[a-zA-Z' -]{0,73}[a-zA-Z])?\\s*$";
    public static final String BOOK_GENRE = "^\\s*[a-zA-Z]+(?:[ -][a-zA-Z]+){0,19}\\s*$";


    private RegexPattern() {
        throw new UnsupportedOperationException(ErrorMessage.NON_INSTANTIABLE_UTILITY_CLASS);
    }
}
