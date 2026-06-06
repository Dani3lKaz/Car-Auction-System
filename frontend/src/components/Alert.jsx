function Alert({ message, type = "danger" }) {
  const error_messages = {
    INVALID_CREDENTIALS: "Nieprawidłowy adres e-mail lub hasło",
    REQUIRED_FIELDS_MISSING: "Uzupełnij wymagane pola",
    INVALID_EMAIL_FORMAT: "Nieprawidłowy adres e-mail",
    WEAK_PASSWORD: "Hasło musi mieć długość min. 8 znaków, min. 1 dużą literę, min. 1 cyfrę",
    PASSWORDS_DO_NOT_MATCH: "Hasła nie są identyczne",
    EMAIL_ALREADY_TAKEN: "Konto o podanym adresie e-mail już istnieje",
    REGISTRATION_FAILED: "Rejestracja nie powiodła się. Spróbuj ponownie.",
    LOGIN_FAILED: "Logowanie nie powiodło się. Spróbuj ponownie."
  };

  return message ? (
    <div className={`alert alert-${type}`} role="alert">
      {error_messages[message]}
    </div>
  ) : null;
}
export default Alert;
