/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import client.ClientView;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import marketplace.Item;
import marketplace.Wish;

/**
 *
 * @author Guillaume
 */
public class TraderImpl extends UnicastRemoteObject implements Trader
{
    private String login;
    private ClientView view;

    public TraderImpl( String login, ClientView view ) throws RemoteException, MalformedURLException
    {
        this.login = login;
        this.view = view;
        Naming.rebind( "rmi://localhost/trader/" + this.login, this );
    }

    @Override
    public void notifySale( Item item ) throws RemoteException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public void notifyWish( Wish wish, Item item ) throws RemoteException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public String getName()
    {
        return this.login;
    }
}
