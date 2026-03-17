package cat.udl.eps.softarch.demo.handler;

import cat.udl.eps.softarch.demo.domain.Asset;
import cat.udl.eps.softarch.demo.domain.User;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class AssetEventHandler {

    @HandleBeforeCreate
    public void handleAssetPreCreate(Asset asset) {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            asset.setCreatedBy(currentUser);
            asset.setLastModifiedBy(currentUser);
        }
    }

    @HandleBeforeSave
    public void handleAssetPreSave(Asset asset) {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            asset.setLastModifiedBy(currentUser);
        }
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        }
        return (User) authentication.getPrincipal();
    }
}

