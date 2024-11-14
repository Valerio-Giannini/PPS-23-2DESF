package mvc.view

trait View:
  /** La view è composta in blocchi in base al cosa sta succedendo nella simulazione ?? app= generico per lanciare la
   * sim init= container dedicato al configurazione dei parametri simulation= container dedicato al render della
   * simulazione in corso report= container dedicato ai grafici della reportistica Mostra a schermo il blocco richiesto
   * dall'utente.
   */

  /** mostra a schermo questa view con i relativi dati
   */
  def show(): Unit

  /** chiudi la view attuale
   */
  def close(): Unit
