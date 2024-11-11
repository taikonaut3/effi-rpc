package io.effi.rpc.governance.router;

import io.effi.rpc.common.constant.Component;
import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.extension.spi.Extension;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.util.StringUtil;
import io.effi.rpc.core.CallInvocation;
import io.effi.rpc.core.Portal;
import io.effi.rpc.core.config.RouterConfig;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link Router}.
 */
@Extension(Component.DEFAULT)
public class DefaultRouter implements Router {

    @Override
    public List<URL> route(CallInvocation<?> invocation, List<URL> urls) {
        URL url = invocation.requestUrl();
        // filter by group
        String group = url.getParam(KeyConstant.GROUP);
        if (!StringUtil.isBlank(group)) {
            urls = urls.stream().filter(item -> Objects.equals(item.getParam(KeyConstant.GROUP), group)).collect(Collectors.toList());
        }
        // filter by router rule
        Portal portal = invocation.portal();
        Collection<RouterConfig> routerConfigs = portal.routerConfigManager().values();
        LinkedList<URL> result = new LinkedList<>();
        boolean hadConfig = false;
        for (RouterConfig routerConfig : routerConfigs) {
            Pattern urlPattern = Pattern.compile(routerConfig.urlRegex());
            if (urlPattern.matcher(url.toString()).find()) {
                hadConfig = true;
                for (URL serviceUrl : urls) {
                    Pattern targetPattern = Pattern.compile(routerConfig.matchTargetRegex());
                    if (targetPattern.matcher(serviceUrl.toString()).find()) {
                        result.add(serviceUrl);
                    }
                }
            }
        }
        return hadConfig ? result : urls;
    }
}
