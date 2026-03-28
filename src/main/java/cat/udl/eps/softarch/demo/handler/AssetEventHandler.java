package cat.udl.eps.softarch.demo.handler;

import cat.udl.eps.softarch.demo.domain.Asset;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.exception.UnsupportedFileExtensionException;
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
        parseAndValidateUrl(asset);

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

    private void parseAndValidateUrl(Asset asset) {
        String url = asset.getUrl();
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL cannot be empty");
        }

        String extension = getExtension(url);
        if (extension == null) {
            throw new UnsupportedFileExtensionException("URL does not have a valid extension");
        }

        String contentType = determineContentType(extension);
        if (contentType == null) {
            throw new UnsupportedFileExtensionException("Unsupported extension: " + extension);
        }

        asset.setContentType(contentType);
    }

    private String getExtension(String url) {
        int i = url.lastIndexOf('.');
        if (i > 0) {
            return url.substring(i + 1).toLowerCase();
        }
        return null; // Or throw exception??
    }

    private String determineContentType(String extension) {
        return switch (extension) {
            case "png" -> "image/png";
            case "jpg", "jpeg" -> "image/jpeg";
            case "pdf" -> "application/pdf";
            case "mp4" -> "video/mp4";
            case "txt" -> "text/plain";
            default -> null;
        };
    }
}
