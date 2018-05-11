/*
    Copyright 2016-2017 Will Winder

    This file is part of Universal Gcode Sender (UGS).

    UGS is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    UGS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with UGS.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.willwinder.universalgcodesender.uielements.panels;

import com.willwinder.universalgcodesender.connection.JSSCConnection;
import com.willwinder.universalgcodesender.connection.JSerialCommConnection;
import com.willwinder.universalgcodesender.i18n.AvailableLanguages;
import com.willwinder.universalgcodesender.i18n.Language;
import com.willwinder.universalgcodesender.i18n.Localization;
import com.willwinder.universalgcodesender.uielements.IChanged;
import com.willwinder.universalgcodesender.uielements.helpers.AbstractUGSSettings;
import com.willwinder.universalgcodesender.utils.Settings;
import com.willwinder.universalgcodesender.utils.SettingsFactory;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

/**
 *
 * @author wwinder
 */
public class ConnectionSettingsPanel extends AbstractUGSSettings {
    private static final String CONNECTION_JSSC = "JSSC";
    private static final String CONNECTION_JSERIALCOMM = "JSerialComm";

    private final Checkbox verboseConsoleOutput = new Checkbox(
                Localization.getString("mainWindow.swing.showVerboseOutputCheckBox"));
    private final Checkbox useZStepSize = new Checkbox(
                Localization.getString("sender.step.separateZ"));
    private final Checkbox singleStepMode = new Checkbox(
                Localization.getString("sender.singlestep"));
    private final Checkbox statusPollingEnabled = new Checkbox(
                Localization.getString("sender.status"));
    private final Spinner statusPollRate = new Spinner(
                Localization.getString("sender.status.rate"),
                new SpinnerNumberModel(1, 1, null, 100));
    private final Checkbox stateColorDisplayEnabled = new Checkbox(
                Localization.getString("sender.state"));
    private final Checkbox showNightlyWarning = new Checkbox(
                Localization.getString("sender.nightly-warning"));
    private final JComboBox<Language> languageCombo = new JComboBox<>(AvailableLanguages.getAvailableLanguages().toArray(new Language[0]));
    private final JComboBox<String> connectionClass = new JComboBox<>(new String[]{CONNECTION_JSSC, CONNECTION_JSERIALCOMM});

    public ConnectionSettingsPanel(Settings settings, IChanged changer) {
        super(settings, changer);
        super.updateComponents();
    }

    public ConnectionSettingsPanel(Settings settings) {
        this(settings, null);
    }

    @Override
    public String getHelpMessage() {
        return Localization.getString("sender.help.verbose.console") + "\n\n" +
                Localization.getString("sender.help.singlestep") + "\n\n" +
                Localization.getString("sender.help.status") + "\n\n" +
                Localization.getString("sender.help.status.rate") + "\n\n" +
                Localization.getString("sender.help.state") + "\n\n";
    }

    @Override
    public void save() {
        settings.setVerboseOutputEnabled(verboseConsoleOutput.getValue());
        settings.setUseZStepSize(useZStepSize.getValue());
        settings.setSingleStepMode(singleStepMode.getValue());
        settings.setStatusUpdatesEnabled(statusPollingEnabled.getValue());
        settings.setStatusUpdateRate((int)statusPollRate.getValue());
        settings.setDisplayStateColor(stateColorDisplayEnabled.getValue());
        //settings.setAutoConnectEnabled(autoConnect.getValue());
        settings.setShowNightlyWarning(showNightlyWarning.getValue());
        settings.setLanguage(((Language)languageCombo.getSelectedItem()).getLanguageCode());
        if (connectionClass.getSelectedItem().equals(CONNECTION_JSERIALCOMM)) {
            settings.setConnectionClass(JSerialCommConnection.class.getCanonicalName());
        } else {
            settings.setConnectionClass(JSSCConnection.class.getCanonicalName());
        }
    }

    @Override
    public void restoreDefaults() throws Exception {
        updateComponents(new Settings());
        SettingsFactory.saveSettings(settings);
        save();
    }

    @Override
    protected void updateComponentsInternal(Settings s) {
        this.removeAll();

        setLayout(new MigLayout("", "fill"));

        verboseConsoleOutput.setSelected(s.isVerboseOutputEnabled());
        add(verboseConsoleOutput, "spanx, wrap");

        useZStepSize.setSelected(s.useZStepSize());
        add(useZStepSize, "spanx, wrap");

        singleStepMode.setSelected(s.isSingleStepMode());
        add(singleStepMode, "spanx, wrap");

        statusPollingEnabled.setSelected(s.isStatusUpdatesEnabled());
        add(statusPollingEnabled, "spanx, wrap");

        statusPollRate.setValue(s.getStatusUpdateRate());
        add(statusPollRate, "spanx, wrap");

        stateColorDisplayEnabled.setSelected(s.isDisplayStateColor());
        add(stateColorDisplayEnabled, "spanx, wrap");

        showNightlyWarning.setSelected(s.isShowNightlyWarning());
        add(showNightlyWarning, "spanx, wrap");

        for (int i = 0; i < languageCombo.getItemCount(); i++) {
            Language l = languageCombo.getItemAt(i);
            if (l.getLanguageCode().equals(s.getLanguage())) {
                languageCombo.setSelectedIndex(i);
                break;
            }
        }
        add(new JLabel(Localization.getString("settings.language")), "gapleft 56");
        add(languageCombo, "grow, wrap");


        if (s.getConnectionClass().equals(JSerialCommConnection.class.getCanonicalName())) {
            connectionClass.setSelectedItem(CONNECTION_JSERIALCOMM);
        } else {
            connectionClass.setSelectedItem(CONNECTION_JSSC);
        }
        add(new JLabel(Localization.getString("settings.connectionDriver")), "gapleft 56");
        add(connectionClass, "grow, wrap");
    }
}
