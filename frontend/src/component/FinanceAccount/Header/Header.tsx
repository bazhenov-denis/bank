import "./Header.css"



export const Header = () => {
    return (
        <header className="header">
            <div className="logo-container">
                <img
                    src="../public/logo_hse.svg"
                    alt="Logo"
                    className="logo"
                />
                <h1 className="title">Учёт финансов</h1>
            </div>
            <button className="user-menu">
                <p>история</p>
            </button>
            <button className="user-menu">
                <p>история</p>
            </button>
            <button className="user-menu">
                <p>история</p>
            </button>
        </header>
    )
}