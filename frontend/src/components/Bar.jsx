import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import Menu from '@mui/material/Menu';
import MenuIcon from '@mui/icons-material/Menu';
import Container from '@mui/material/Container';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import Tooltip from '@mui/material/Tooltip';
import MenuItem from '@mui/material/MenuItem';
import AdbIcon from '@mui/icons-material/Adb';
import { useQueryClient } from '@tanstack/react-query'
import { Link, useNavigate } from 'react-router-dom';
import { useCurrentUser } from '../hooks/useCurrentUser.js'
import { logout } from '../api/userApi.js'

const pages = [
    {title: "Strona główna", path:"/"},
    {title: "Aukcje", path:"/auctions"},
    {title: "Pomoc", path:"/help"}
];

function Bar() {
    const [anchorElUser, setAnchorElUser] = React.useState(null);
    const {data: user, isLoading } = useCurrentUser();
    const queryClient = useQueryClient();
    const navigate = useNavigate();

    const handleOpenUserMenu = (event) => {
        setAnchorElUser(event.currentTarget);
    };

    const handleCloseUserMenu = () => {
        setAnchorElUser(null);
    };

    const handleLogout = async () => {
        await logout()
        queryClient.setQueryData(["currentUser"], null)
        handleCloseUserMenu();
        navigate("/")
    }

    const settings = [
        {title: "Profil", action: () => {}},
        {title: "Wyloguj", action: handleLogout}
    ];

    return (
        <AppBar position="static" sx={{ backgroundColor: 'primary.main'}}>
            <Container maxWidth="xxl">
                <Toolbar disableGutters>
                    <Typography
                        variant="h6"
                        noWrap
                        component={Link}
                        to="/"
                        href="#app-bar-with-responsive-menu"
                        sx={{
                            mr: 2,
                            display: { xs: 'none', md: 'flex' },
                            fontFamily: 'monospace',
                            fontWeight: 700,
                            letterSpacing: '.3rem',
                            color: 'inherit',
                            textDecoration: 'none',
                        }}
                    >
                        MotoTrade
                    </Typography>
                    <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}>
                        {pages.map((page) => (
                            <Button
                                key={page.title}
                                component={Link}
                                to={page.path}
                                sx={{ my: 2, color: 'white', display: 'block' }}
                            >
                                {page.title}
                            </Button>
                        ))}
                    </Box>
                    <Box sx={{ flexGrow: 0 }}>
                        {isLoading ? null : user ? (
                            <>
                                <Tooltip title="Open settings">
                                    Cześć { user.firstName }!
                                    <IconButton onClick={handleOpenUserMenu} sx={{ p: 0, mx: 2}}>
                                        <Avatar>{user.firstName[0]}</Avatar>
                                    </IconButton>
                                </Tooltip>
                                <Menu
                                    sx={{ mt: '45px' }}
                                    id="menu-appbar"
                                    anchorEl={anchorElUser}
                                    anchorOrigin={{
                                        vertical: 'top',
                                        horizontal: 'right',
                                    }}
                                    keepMounted
                                    transformOrigin={{
                                        vertical: 'top',
                                        horizontal: 'right',
                                    }}
                                    open={Boolean(anchorElUser)}
                                    onClose={handleCloseUserMenu}
                                >
                                    {settings.map((setting) => (
                                        <MenuItem key={setting.title} onClick={setting.action}>
                                            <Typography sx={{ textAlign: 'center' }}>{setting.title}</Typography>
                                        </MenuItem>
                                    ))}
                                </Menu>
                            </>
                        ) : (
                            <div>
                                <Button variant="contained" component={Link} to="/login" color="secondary">Zaloguj się</Button>
                            </div>
                        )}
                    </Box>
                </Toolbar>
            </Container>
        </AppBar>
    );
}
export default Bar;