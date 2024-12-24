
import '../app/styles/sidebar.css'

interface SideBarProps {
    onClose: () => void;
    items: string[];
    links: string[];
    openClick: () => void,
    isOpen: boolean;
}

const SideBar = ({ onClose, items, links, openClick, isOpen }: SideBarProps) => {
    return (
        <>
            <div id="mySidenav" className="sidenav" style={{ width: isOpen ? '250px' : '0' }}>
                <a href="#" className="closebtn" onClick={onClose}>&times;</a>
                {links.map((l, index) =>
                    <a href={l}
                        key={index}>{items[index]}</a>
                )}

            </div>
            <span onClick={openClick} style={{ cursor: 'pointer', fontSize: '36px' }}>
                &#9776;
            </span>
        </>
    )

};

export default SideBar;
