/**
 * generated by Xtext 2.15.0
 */
package org.edla.xtext.tptp;

import org.edla.xtext.tptp.TptpStandaloneSetupGenerated;

/**
 * Initialization support for running Xtext languages without Equinox extension registry.
 */
@SuppressWarnings("all")
public class TptpStandaloneSetup extends TptpStandaloneSetupGenerated {
  public static void doSetup() {
    new TptpStandaloneSetup().createInjectorAndDoEMFRegistration();
  }
}
