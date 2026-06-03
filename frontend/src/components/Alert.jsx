function Alert({ message, type = "danger" }) {
  const error_messages = {
    INVALID_CREDENTIALS: "Nieprawidłowy adres e-mail lub hasło",
  };

  return message ? (
    <div className={`alert alert-${type}`} role="alert">
      {error_messages[message]}
    </div>
  ) : null;
}
export default Alert;
