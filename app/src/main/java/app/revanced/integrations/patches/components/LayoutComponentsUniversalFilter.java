package app.revanced.integrations.patches.components;

import app.revanced.integrations.settings.SettingsEnum;

final class LayoutComponentsUniversalFilter extends Filter {

    public LayoutComponentsUniversalFilter() {
        final StringFilterGroup expandableMetadata = new StringFilterGroup(
                SettingsEnum.HIDE_EXPANDABLE_CHIP,
                "inline_expander"
        );

        final StringFilterGroup feedSurvey = new StringFilterGroup(
                SettingsEnum.HIDE_FEED_SURVEY,
                "feed_nudge",
                "in_feed_survey",
                "slimline_survey"
        );

        final StringFilterGroup grayDescription = new StringFilterGroup(
                SettingsEnum.HIDE_GRAY_DESCRIPTION,
                "endorsement_header_footer"
        );

        final StringFilterGroup timedReactions = new StringFilterGroup(
                SettingsEnum.HIDE_TIMED_REACTIONS,
                "emoji_control_panel",
                "timed_reaction"
        );

        pathFilterGroupList.addAll(
                expandableMetadata,
                feedSurvey,
                grayDescription,
                timedReactions
        );
    }
}
