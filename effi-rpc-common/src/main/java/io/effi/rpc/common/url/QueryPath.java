package io.effi.rpc.common.url;

import io.effi.rpc.common.util.StringUtil;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * Parsed URL query path, containing the path segments and query parameters.
 */
@Getter
@Accessors(fluent = true)
public class QueryPath {

    private final List<String> paths;

    private final Map<String, String> queryParams;

    /**
     * Constructs a QueryPath with the specified path and query parameters.
     *
     * @param path   the path part of the URL
     * @param params the query parameters as a Map
     */
    QueryPath(String path, Map<String, String> params) {
        // Convert the path string to a list of segments
        this.paths = URLUtil.toPaths(path);
        this.queryParams = params;
    }

    /**
     * Creates a QueryPath instance from the input string.
     *
     * @param input the input URL string (may include path and params)
     * @return a QueryPath object containing the parsed paths and query parameters
     */
    public static QueryPath valueOf(String input) {
        return URLUtil.buildQueryPath(input);
    }

    /**
     * Converts the stored path segments back into a single path string.
     *
     * @return the reconstructed path string
     */
    public String path() {
        return URLUtil.toPath(paths);
    }

    /**
     * Returns a string representation of the QueryPath, combining the path and query parameters.
     *
     * @return a string representation of the QueryPath
     */
    @Override
    public String toString() {
        String path = path();
        String queryParam = URLUtil.toQueryParam(queryParams);
        // If the path is empty, return only the query parameters; otherwise, return the full query path format
        if (StringUtil.isBlank(path)) {
            return queryParam;
        } else {
            return path + (StringUtil.isBlank(queryParam) ? "" : ("?" + queryParam));
        }
    }
}


