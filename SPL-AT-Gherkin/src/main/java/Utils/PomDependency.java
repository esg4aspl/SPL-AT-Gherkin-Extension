package Utils;

/**
 * @author sercansensulun on 28.07.2020.
 */
public class PomDependency {

    private String groupId;
    private String artifactId;
    private String version;
    private String scope;


    public PomDependency(String groupId, String artifactId, String version, String scope) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.scope = scope;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getScope() {
        return scope;
    }

    public String getVersion() {
        return version;
    }
}
