package beforespring.socialfeed.content.domain;

public enum ContentSourceType {
    FACEBOOK,
    TWITTER,
    INSTAGRAM,
    THREADS;

    /**
     * @return 소문자로 반환(ex: FACEBOOK -> facebook)
     */
    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
