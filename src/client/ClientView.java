/*
 * ClientView.java
 */
package client;

import bank.exception.RejectedException;
import bank.rmi.Account;
import bank.rmi.Bank;
import client.rmi.impl.TraderImpl;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import marketplace.rmi.MarketPlace;
import marketplace.rmi.Trader;


/**
 * The application's main frame.
 */
public class ClientView extends FrameView
{
    private static final String DEFAULT_BANK_NAME = "Nordea";
    private static final String DEFAULT_MARKET_NAME = "Agora";
    private Bank bankobj;
    private Account account;
    private MarketPlace market;
    private Trader trader = null;

    public ClientView( SingleFrameApplication app, String[] args )
    {
        super( app );

        initComponents();

        /**
         * Hide dialogs
         */
        marketPlaceConnectionDialog.setVisible( false );
        accountCreationDialog.setVisible( false );
        disconnectMenuItem.setVisible( false );
        marketPlacePanel.setVisible( false );

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger( "StatusBar.messageTimeout" );
        messageTimer = new Timer( messageTimeout, new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                statusMessageLabel.setText( "" );
            }
        } );
        messageTimer.setRepeats( false );
        int busyAnimationRate = resourceMap.getInteger( "StatusBar.busyAnimationRate" );
        for ( int i = 0; i < busyIcons.length; i++ )
        {
            busyIcons[i] = resourceMap.getIcon( "StatusBar.busyIcons[" + i + "]" );
        }
        busyIconTimer = new Timer( busyAnimationRate, new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                busyIconIndex = ( busyIconIndex + 1 ) % busyIcons.length;
                statusAnimationLabel.setIcon( busyIcons[busyIconIndex] );
            }
        } );
        idleIcon = resourceMap.getIcon( "StatusBar.idleIcon" );
        statusAnimationLabel.setIcon( idleIcon );
        progressBar.setVisible( false );

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor( getApplication().getContext() );
        taskMonitor.addPropertyChangeListener( new java.beans.PropertyChangeListener()
        {
            public void propertyChange( java.beans.PropertyChangeEvent evt )
            {
                String propertyName = evt.getPropertyName();
                switch ( propertyName )
                {
                    case "started":
                        if ( !busyIconTimer.isRunning() )
                        {
                            statusAnimationLabel.setIcon( busyIcons[0] );
                            busyIconIndex = 0;
                            busyIconTimer.start();
                        }
                        progressBar.setVisible( true );
                        progressBar.setIndeterminate( true );
                        break;
                    case "done":
                        busyIconTimer.stop();
                        statusAnimationLabel.setIcon( idleIcon );
                        progressBar.setVisible( false );
                        progressBar.setValue( 0 );
                        break;
                    case "message":
                        String text = ( String ) ( evt.getNewValue() );
                        statusMessageLabel.setText( ( text == null ) ? "" : text );
                        messageTimer.restart();
                        break;
                    case "progress":
                        int value = ( Integer ) ( evt.getNewValue() );
                        progressBar.setVisible( true );
                        progressBar.setIndeterminate( false );
                        progressBar.setValue( value );
                        break;
                }
            }
        } );

        /**
         * Binding to the bank
         */
        String bankname = "";
        if ( args.length != 0 )
        {
            bankname = args[0];
        } else
        {
            bankname = DEFAULT_BANK_NAME;
        }

        try
        {
            bankobj = ( Bank ) Naming.lookup( bankname );
        } catch ( NotBoundException | MalformedURLException | RemoteException e )
        {
            System.out.println( "The runtime failed: " + e.getMessage() );
            System.exit( 0 );
        }

        /**
         * Binding to the marketplace
         */
        String marketName = "";
        if ( args.length > 1 )
        {
            marketName = args[1];
        } else
        {
            marketName = DEFAULT_MARKET_NAME;
        }
        try
        {
            market = ( MarketPlace ) Naming.lookup( marketName );
        } catch ( NotBoundException | MalformedURLException | RemoteException e )
        {
            System.out.println( "The runtime failed: " + e.getMessage() );
            System.exit( 0 );
        }
    }

    @Action
    public void showAboutBox()
    {
        if ( aboutBox == null )
        {
            JFrame mainFrame = ClientApp.getApplication().getMainFrame();
            aboutBox = new ClientAboutBox( mainFrame );
            aboutBox.setLocationRelativeTo( mainFrame );
        }
        ClientApp.getApplication().show( aboutBox );
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        marketPlacePanel = new javax.swing.JPanel();
        testLabel = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        createAccountMenuItem = new javax.swing.JMenuItem();
        marketplaceMenuItem = new javax.swing.JMenuItem();
        disconnectMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        accountCreationDialog = new javax.swing.JDialog();
        accountIdTextfield = new javax.swing.JTextField();
        accountTitleLabel = new javax.swing.JLabel();
        accountIdLabel = new javax.swing.JLabel();
        accountDepositTextfield = new javax.swing.JTextField();
        accountDepositLabel = new javax.swing.JLabel();
        accountOkButton = new javax.swing.JButton();
        accountCancelButton = new javax.swing.JButton();
        accountErrorLabel = new javax.swing.JLabel();
        marketPlaceConnectionDialog = new javax.swing.JDialog();
        marketPlaceConnectionTitle = new javax.swing.JLabel();
        marketPlaceConnectionLoginLabel = new javax.swing.JLabel();
        loginTextfield = new javax.swing.JTextField();
        connectionButton = new javax.swing.JButton();
        connectionCancelButton = new javax.swing.JButton();
        connectionErrorLabel = new javax.swing.JLabel();

        mainPanel.setMinimumSize(new java.awt.Dimension(656, 388));
        mainPanel.setName("mainPanel"); // NOI18N

        marketPlacePanel.setName("marketPlacePanel"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(client.ClientApp.class).getContext().getResourceMap(ClientView.class);
        testLabel.setFont(resourceMap.getFont("testLabel.font")); // NOI18N
        testLabel.setText(resourceMap.getString("testLabel.text")); // NOI18N
        testLabel.setName("testLabel"); // NOI18N

        javax.swing.GroupLayout marketPlacePanelLayout = new javax.swing.GroupLayout(marketPlacePanel);
        marketPlacePanel.setLayout(marketPlacePanelLayout);
        marketPlacePanelLayout.setHorizontalGroup(
            marketPlacePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(marketPlacePanelLayout.createSequentialGroup()
                .addGap(133, 133, 133)
                .addComponent(testLabel)
                .addContainerGap(144, Short.MAX_VALUE))
        );
        marketPlacePanelLayout.setVerticalGroup(
            marketPlacePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(marketPlacePanelLayout.createSequentialGroup()
                .addGap(146, 146, 146)
                .addComponent(testLabel)
                .addContainerGap(198, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(marketPlacePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(marketPlacePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        createAccountMenuItem.setText(resourceMap.getString("AccountCreationMenuItem.text")); // NOI18N
        createAccountMenuItem.setName("AccountCreationMenuItem"); // NOI18N
        createAccountMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createAccountMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(createAccountMenuItem);

        marketplaceMenuItem.setText(resourceMap.getString("ConnectionIMenuItem.text")); // NOI18N
        marketplaceMenuItem.setName("ConnectionIMenuItem"); // NOI18N
        marketplaceMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                marketplaceMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(marketplaceMenuItem);

        disconnectMenuItem.setText(resourceMap.getString("disconnectMenuItem.text")); // NOI18N
        disconnectMenuItem.setName("disconnectMenuItem"); // NOI18N
        disconnectMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disconnectMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(disconnectMenuItem);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(client.ClientApp.class).getContext().getActionMap(ClientView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 656, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 486, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        accountCreationDialog.setMinimumSize(new java.awt.Dimension(235, 245));
        accountCreationDialog.setName("accountCreationDialog"); // NOI18N

        accountIdTextfield.setText(resourceMap.getString("accountIdTextfield.text")); // NOI18N
        accountIdTextfield.setName("accountIdTextfield"); // NOI18N

        accountTitleLabel.setFont(resourceMap.getFont("accountTitleLabel.font")); // NOI18N
        accountTitleLabel.setText(resourceMap.getString("accountTitleLabel.text")); // NOI18N
        accountTitleLabel.setName("accountTitleLabel"); // NOI18N

        accountIdLabel.setText(resourceMap.getString("accountIdLabel.text")); // NOI18N
        accountIdLabel.setName("accountIdLabel"); // NOI18N

        accountDepositTextfield.setText(resourceMap.getString("accountDepositTextfield.text")); // NOI18N
        accountDepositTextfield.setName("accountDepositTextfield"); // NOI18N

        accountDepositLabel.setText(resourceMap.getString("accountDepositLabel.text")); // NOI18N
        accountDepositLabel.setName("accountDepositLabel"); // NOI18N

        accountOkButton.setText(resourceMap.getString("accountOkButton.text")); // NOI18N
        accountOkButton.setName("accountOkButton"); // NOI18N
        accountOkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accountOkButtonActionPerformed(evt);
            }
        });

        accountCancelButton.setText(resourceMap.getString("accountCancelButton.text")); // NOI18N
        accountCancelButton.setName("accountCancelButton"); // NOI18N
        accountCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accountCancelButtonActionPerformed(evt);
            }
        });

        accountErrorLabel.setForeground(resourceMap.getColor("accountErrorLabel.foreground")); // NOI18N
        accountErrorLabel.setText(resourceMap.getString("accountErrorLabel.text")); // NOI18N
        accountErrorLabel.setName("accountErrorLabel"); // NOI18N

        javax.swing.GroupLayout accountCreationDialogLayout = new javax.swing.GroupLayout(accountCreationDialog.getContentPane());
        accountCreationDialog.getContentPane().setLayout(accountCreationDialogLayout);
        accountCreationDialogLayout.setHorizontalGroup(
            accountCreationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accountCreationDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(accountCreationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(accountErrorLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, accountCreationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(accountDepositTextfield, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(accountIdTextfield, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                        .addComponent(accountTitleLabel, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(accountIdLabel, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(accountDepositLabel, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, accountCreationDialogLayout.createSequentialGroup()
                        .addComponent(accountOkButton, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(accountCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        accountCreationDialogLayout.setVerticalGroup(
            accountCreationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accountCreationDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(accountTitleLabel)
                .addGap(24, 24, 24)
                .addComponent(accountIdLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(accountIdTextfield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addComponent(accountDepositLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(accountDepositTextfield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(accountCreationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(accountOkButton)
                    .addComponent(accountCancelButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(accountErrorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE))
        );

        marketPlaceConnectionDialog.setMinimumSize(new java.awt.Dimension(279, 171));
        marketPlaceConnectionDialog.setName("marketPlaceConnectionDialog"); // NOI18N

        marketPlaceConnectionTitle.setFont(resourceMap.getFont("marketPlaceConnectionTitle.font")); // NOI18N
        marketPlaceConnectionTitle.setText(resourceMap.getString("marketPlaceConnectionTitle.text")); // NOI18N
        marketPlaceConnectionTitle.setName("marketPlaceConnectionTitle"); // NOI18N

        marketPlaceConnectionLoginLabel.setText(resourceMap.getString("marketPlaceConnectionLoginLabel.text")); // NOI18N
        marketPlaceConnectionLoginLabel.setName("marketPlaceConnectionLoginLabel"); // NOI18N

        loginTextfield.setText(resourceMap.getString("loginTextfield.text")); // NOI18N
        loginTextfield.setName("loginTextfield"); // NOI18N

        connectionButton.setText(resourceMap.getString("connectionButton.text")); // NOI18N
        connectionButton.setName("connectionButton"); // NOI18N
        connectionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectionButtonActionPerformed(evt);
            }
        });

        connectionCancelButton.setText(resourceMap.getString("connectionCancelButton.text")); // NOI18N
        connectionCancelButton.setName("connectionCancelButton"); // NOI18N
        connectionCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectionCancelButtonActionPerformed(evt);
            }
        });

        connectionErrorLabel.setText(resourceMap.getString("connectionErrorLabel.text")); // NOI18N
        connectionErrorLabel.setName("connectionErrorLabel"); // NOI18N
        connectionErrorLabel.setPreferredSize(new java.awt.Dimension(50, 230));

        javax.swing.GroupLayout marketPlaceConnectionDialogLayout = new javax.swing.GroupLayout(marketPlaceConnectionDialog.getContentPane());
        marketPlaceConnectionDialog.getContentPane().setLayout(marketPlaceConnectionDialogLayout);
        marketPlaceConnectionDialogLayout.setHorizontalGroup(
            marketPlaceConnectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(marketPlaceConnectionDialogLayout.createSequentialGroup()
                .addGroup(marketPlaceConnectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(marketPlaceConnectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(marketPlaceConnectionDialogLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(marketPlaceConnectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(marketPlaceConnectionTitle)
                                .addGroup(marketPlaceConnectionDialogLayout.createSequentialGroup()
                                    .addComponent(marketPlaceConnectionLoginLabel)
                                    .addGap(18, 18, 18)
                                    .addComponent(loginTextfield))))
                        .addGroup(marketPlaceConnectionDialogLayout.createSequentialGroup()
                            .addGap(25, 25, 25)
                            .addComponent(connectionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(connectionCancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(marketPlaceConnectionDialogLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(connectionErrorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)))
                .addContainerGap())
        );
        marketPlaceConnectionDialogLayout.setVerticalGroup(
            marketPlaceConnectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(marketPlaceConnectionDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(marketPlaceConnectionTitle)
                .addGap(29, 29, 29)
                .addGroup(marketPlaceConnectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(marketPlaceConnectionLoginLabel)
                    .addComponent(loginTextfield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(marketPlaceConnectionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(connectionButton)
                    .addComponent(connectionCancelButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(connectionErrorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void createAccountMenuItemActionPerformed( java.awt.event.ActionEvent evt )//GEN-FIRST:event_createAccountMenuItemActionPerformed
    {//GEN-HEADEREND:event_createAccountMenuItemActionPerformed
        accountCreationDialog.setVisible( true );
        accountIdTextfield.setText( "" );
        accountDepositTextfield.setText( "0" );
        accountErrorLabel.setText( "" );

    }//GEN-LAST:event_createAccountMenuItemActionPerformed

    private void accountOkButtonActionPerformed( java.awt.event.ActionEvent evt )//GEN-FIRST:event_accountOkButtonActionPerformed
    {//GEN-HEADEREND:event_accountOkButtonActionPerformed
        String id = accountIdTextfield.getText();
        String deposit = accountDepositTextfield.getText();
        int depositNumber = 0;

        /**
         * Test if the id field is empty
         */
        if ( id.isEmpty() )
        {
            accountIdTextfield.requestFocus();
            accountErrorLabel.setText( "The account id must not be empty" );
            return;
        }
        /**
         * Test if the number given for the deposit is valid
         */
        try
        {
            depositNumber = Integer.parseInt( deposit );
        } catch ( NumberFormatException ne )
        {
            accountDepositTextfield.requestFocus();
            accountErrorLabel.setText( "Please give a valid number" );
            return;
        }
        /**
         * Try to create the account
         */
        try
        {
            bankobj.newAccount( id );
        } catch ( RemoteException ex )
        {
            accountErrorLabel.setText( "Error during the transaction with the bank" );
        } catch ( RejectedException ex )
        {
            accountIdTextfield.requestFocus();
            accountErrorLabel.setText( "The account id already exists" );
        }
        /**
         * Give the initial deposit to the new account
         */
        try
        {
            bankobj.getAccount( id ).deposit( depositNumber );
        } catch ( RemoteException | RejectedException ex )
        {
            ex.printStackTrace();
        }
        accountIdTextfield.setText( "" );
        accountDepositTextfield.setText( "0" );
        accountErrorLabel.setText( "" );
        accountCreationDialog.setVisible( false );
    }//GEN-LAST:event_accountOkButtonActionPerformed

    private void accountCancelButtonActionPerformed( java.awt.event.ActionEvent evt )//GEN-FIRST:event_accountCancelButtonActionPerformed
    {//GEN-HEADEREND:event_accountCancelButtonActionPerformed
        accountIdTextfield.setText( "" );
        accountDepositTextfield.setText( "" );
        accountErrorLabel.setText( "" );
        accountCreationDialog.setVisible( false );
    }//GEN-LAST:event_accountCancelButtonActionPerformed

    private void connectionButtonActionPerformed( java.awt.event.ActionEvent evt )//GEN-FIRST:event_connectionButtonActionPerformed
    {//GEN-HEADEREND:event_connectionButtonActionPerformed
        //TODO handle the connection with the marketplace
        try
        {
            try
            {
                this.trader = new TraderImpl( loginTextfield.getText() , this );
            } catch (    RemoteException | MalformedURLException ex )
            {
                ex.printStackTrace();
            }
            market.registerClient( this.trader );
        } catch ( marketplace.exception.RejectedException ex )
        {
            connectionErrorLabel.setText( "This user is already logged in the system." );
            return;
        } catch ( RemoteException re )
        {
            connectionErrorLabel.setText( "Problem during the transaction with the marketplace." );
            re.printStackTrace();
            return;
        }

        disconnectMenuItem.setVisible( true );
        marketplaceMenuItem.setVisible( false );
        marketPlaceConnectionDialog.setVisible( false );
        loginTextfield.setText( "" );
    }//GEN-LAST:event_connectionButtonActionPerformed

    private void connectionCancelButtonActionPerformed( java.awt.event.ActionEvent evt )//GEN-FIRST:event_connectionCancelButtonActionPerformed
    {//GEN-HEADEREND:event_connectionCancelButtonActionPerformed
        marketPlaceConnectionDialog.setVisible( false );
        loginTextfield.setText( "" );
        connectionErrorLabel.setText( "" );
    }//GEN-LAST:event_connectionCancelButtonActionPerformed

    private void marketplaceMenuItemActionPerformed( java.awt.event.ActionEvent evt )//GEN-FIRST:event_marketplaceMenuItemActionPerformed
    {//GEN-HEADEREND:event_marketplaceMenuItemActionPerformed
        marketPlaceConnectionDialog.setVisible( true );
    }//GEN-LAST:event_marketplaceMenuItemActionPerformed

    private void disconnectMenuItemActionPerformed( java.awt.event.ActionEvent evt )//GEN-FIRST:event_disconnectMenuItemActionPerformed
    {//GEN-HEADEREND:event_disconnectMenuItemActionPerformed
        try
        {
            this.market.unRegisterClient( this.trader );
        } catch ( RemoteException ex )
        {
            ex.printStackTrace();
            return;
        } catch ( marketplace.exception.RejectedException ex )
        {
            ex.printStackTrace();
            return;
        }
        this.trader = null;
        marketPlacePanel.setVisible( false );
        disconnectMenuItem.setVisible( false );
        marketplaceMenuItem.setVisible( true );
    }//GEN-LAST:event_disconnectMenuItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton accountCancelButton;
    private javax.swing.JDialog accountCreationDialog;
    private javax.swing.JLabel accountDepositLabel;
    private javax.swing.JTextField accountDepositTextfield;
    private javax.swing.JLabel accountErrorLabel;
    private javax.swing.JLabel accountIdLabel;
    private javax.swing.JTextField accountIdTextfield;
    private javax.swing.JButton accountOkButton;
    private javax.swing.JLabel accountTitleLabel;
    private javax.swing.JButton connectionButton;
    private javax.swing.JButton connectionCancelButton;
    private javax.swing.JLabel connectionErrorLabel;
    private javax.swing.JMenuItem createAccountMenuItem;
    private javax.swing.JMenuItem disconnectMenuItem;
    private javax.swing.JTextField loginTextfield;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JDialog marketPlaceConnectionDialog;
    private javax.swing.JLabel marketPlaceConnectionLoginLabel;
    private javax.swing.JLabel marketPlaceConnectionTitle;
    private javax.swing.JPanel marketPlacePanel;
    private javax.swing.JMenuItem marketplaceMenuItem;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JLabel testLabel;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[ 15 ];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
}
